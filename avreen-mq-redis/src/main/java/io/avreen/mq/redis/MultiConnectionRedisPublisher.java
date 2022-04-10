package io.avreen.mq.redis;

import io.avreen.common.context.MsgContext;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.TPS;
import io.avreen.mq.api.RemoteMsgPublisherAbstract;
import io.avreen.mq.api.StatisticUtil;
import io.avreen.redis.common.RedisClientConfigProperties;
import io.avreen.redis.common.RedisCommandsUtil;
import io.avreen.redis.common.RedisMetricMonitor;
import io.avreen.redis.common.RedisUtil;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.ZAddArgs;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.async.RedisListAsyncCommands;
import io.lettuce.core.api.async.RedisSortedSetAsyncCommands;
import io.lettuce.core.api.sync.RedisListCommands;
import io.lettuce.core.api.sync.RedisSortedSetCommands;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import javax.management.DescriptorKey;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

/**
 * The class Redis publisher.
 *
 * @param <M> the type parameter
 */
class MultiConnectionRedisPublisher<M> extends RemoteMsgPublisherAbstract<M> implements RedisPublisherMXBean {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".reactor.messagebroker.impl.redis.RedisPublisher");
    private boolean publishAsync = true;
    private Duration bufferOfferDuration = Duration.ofSeconds(1);
    private int bufferSubscribeHelperThreadCount = 1;
    private AtomicLong totalTxBytes = new AtomicLong(0);
    private RedisClientConfigProperties redisClientConfig;
    private int connectionCount = 1;
    private StatefulConnection<String, byte[]>[] connect;
    private AtomicLong atomicLong = new AtomicLong(0);

    private ConcurrentHashMap<String, AtomicLong> queue_tx_bytes = new ConcurrentHashMap<>();
    private RedisMetricMonitor redisMetricMonitor;
    private TPS tps = new TPS();
    private boolean useZCommands = false;

    //private RedisListAsyncCommands<String, byte[]> listAsyncCommands;
    //private RedisListCommands<String, byte[]> listCommands;
    //private RedisSortedSetAsyncCommands<String, byte[]> sortedSetAsyncCommands;
    //private RedisSortedSetCommands<String, byte[]> sortedSetCommands;


    /**
     * Instantiates a new Redis publisher.
     *
     * @param redisClientConfig the redis client config
     */
    public MultiConnectionRedisPublisher(RedisClientConfigProperties redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    @Override
    public long getTotalTxBytes() {
        return totalTxBytes.get();
    }

    public Duration getBufferOfferDuration() {
        return bufferOfferDuration;
    }

    public String getBufferOfferDurationString() {
        if (bufferOfferDuration == null)
            return "-";
        return bufferOfferDuration.toString();
    }

    public void setBufferOfferDuration(Duration bufferOfferDuration) {
        this.bufferOfferDuration = bufferOfferDuration;
    }

    public int getBufferSubscribeHelperThreadCount() {
        return bufferSubscribeHelperThreadCount;
    }

    public void setBufferSubscribeHelperThreadCount(int bufferSubscribeHelperThreadCount) {
        this.bufferSubscribeHelperThreadCount = bufferSubscribeHelperThreadCount;
    }


    public boolean isPublishAsync() {
        return publishAsync;
    }

    public void setPublishAsync(boolean publishAsync) {
        this.publishAsync = publishAsync;
    }

    @Override
    protected void publishBytes(String queueName, MsgContext<M>[] msgContext, byte[][] bytes) {
        StatefulConnection<String, byte[]> connect = checkConnection();
        AtomicLong atomicLong_tx_bytes = StatisticUtil.getCounter(queueName, queue_tx_bytes);
        for (byte[] a : bytes) {
            totalTxBytes.addAndGet(a.length);
            atomicLong_tx_bytes.addAndGet(a.length);
        }


        if (!useZCommands) {
            if (isPublishAsync()) {
                RedisListAsyncCommands<String, byte[]> listAsyncCommands = RedisCommandsUtil.getAsyncListCommands(connect);
                listAsyncCommands.rpush(queueName, bytes).whenComplete((aLong, throwable) -> {
                    if (throwable != null)
                        LOGGER.error(throwable.getMessage(), throwable);
                });
            } else {
                RedisListCommands<String, byte[]> listCommands = RedisCommandsUtil.getListCommands(connect);
                listCommands.rpush(queueName, bytes);
            }
        } else {
            ScoredValue<byte[]>[] values = new ScoredValue[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                values[i] = ScoredValue.just((double) System.currentTimeMillis(), bytes[i]);
            }
            if (isPublishAsync()) {
                RedisSortedSetAsyncCommands<String, byte[]> sortedSetAsyncCommands = RedisCommandsUtil.getAsyncSortedSetCommands(connect);
                sortedSetAsyncCommands.zadd(queueName, ZAddArgs.Builder.nx(), values).whenComplete(new BiConsumer<Long, Throwable>() {
                    @Override
                    public void accept(Long aLong, Throwable throwable) {
                        if (throwable != null)
                            LOGGER.error(throwable.getMessage(), throwable);
                    }
                });
            } else {
                RedisSortedSetCommands<String, byte[]> sortedSetCommands = RedisCommandsUtil.getSortedSetCommands(connect);
                sortedSetCommands.zadd(queueName, ZAddArgs.Builder.nx(), values);
            }

        }
        tps.tick();

    }

    public String getQueueTxBytes() {
        return StatisticUtil.toString(queue_tx_bytes);
    }

    @Override
    public String getType() {
        return "RedisPublisher";
    }


    public String getClientSessionName() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        return "publisher::" + getName() + ":" + jvmName;
    }

    @Override
    @DescriptorKey("ddd")
    public String executeCommand(String fullCommand) {
        return redisMetricMonitor.executeCommand(fullCommand);
    }

    @Override
    public String getTps() {
        return tps.toString();
    }

    @Override
    public String getBindRedisAddress() {
        return redisMetricMonitor.configGet("bind").toString();
    }

    @Override
    public String getBindRedisPort() {
        return redisMetricMonitor.configGet("port").toString();
    }


    @Override
    protected void startService() throws Throwable {
        super.startService();
        redisMetricMonitor = new RedisMetricMonitor("publisher." + getName(), redisClientConfig);
        connect = new StatefulConnection[connectionCount];
        for (int idx = 0; idx < connect.length; idx++) {
            connect[idx] = RedisCommandsUtil.connect(redisClientConfig);
            RedisUtil.setSessionInfo(connect[idx], getClientSessionName());
        }
    }

    @Override
    public String getSessionInfo() {
        List<Map<String, String>> clientInfo = redisMetricMonitor.getClientInfo(getClientSessionName());
        if (clientInfo == null || clientInfo.size() == 0)
            return null;
        return clientInfo.get(0).toString();
    }

    public Long getPendingCount(String queue) {
        return redisMetricMonitor.getPendingCount(queue);
    }

    public List<String> getLastMessage(String queue) {
        return redisMetricMonitor.getLastMessage(queue);

    }

    private StatefulConnection<String, byte[]> checkConnection() {
        if (connect == null)
            throw new RuntimeException("redis connection not init for service");
        StatefulConnection<String, byte[]> returnConnection;
        if (connectionCount == 1)
            returnConnection = connect[0];
        else {
            int cn = (int) (atomicLong.incrementAndGet() % connectionCount);
            returnConnection = connect[cn];
        }
        if (returnConnection == null)
            throw new RuntimeException("returnConnection connection not init for service in publisher");
        return returnConnection;
    }

    public List<String> getLastMessages(String queue, long start, long end) {
        return redisMetricMonitor.getLastMessages(queue, start, end);
    }

    @Override
    public String ping() {
        return redisMetricMonitor.ping();
    }

    @Override
    public long getSessionClientID() {
        return redisMetricMonitor.getClientID(getClientSessionName());
    }

    /**
     * Avreen Project create by hadi asgharnejad khoee
     * Copyright (C) 2017-2020 j
     * <p>
     * This program is free software: you can redistribute it and/or modify
     * it under the terms of the GNU Affero General Public License as
     * published by the Free Software Foundation, either version 1 of the
     * License, or (at your option) any later version.
     * <p>
     * This program is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
     * GNU Affero General Public License for more details.
     * <p>
     * You should have received a copy of the GNU Affero General Public License
     * along with this program. If not, see <http://www.gnu.org/licenses/>.
     * The type Buffer message.
     */

}
