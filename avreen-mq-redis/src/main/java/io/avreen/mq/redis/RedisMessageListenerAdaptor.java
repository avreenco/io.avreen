package io.avreen.mq.redis;

import io.avreen.common.actor.ActorBase;
import io.avreen.common.limiter.IRateLimiter;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.TPS;
import io.avreen.mq.api.IMsgConsumer;
import io.avreen.redis.common.RedisClientConfigProperties;
import io.avreen.redis.common.RedisCommandsUtil;
import io.avreen.redis.common.RedisMetricMonitor;
import io.avreen.redis.common.RedisUtil;
import io.lettuce.core.KeyValue;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.sync.RedisListCommands;
import io.lettuce.core.api.sync.RedisScriptingCommands;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The class Redis message listener adaptor.
 */
class RedisMessageListenerAdaptor extends ActorBase implements RedisMessageListenerAdaptorMXBean {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".reactor.messagebroker.impl.redis.RedisMessageListenerAdaptor");
    private String queue;
    private IMsgConsumer msgConsumer;
    private Thread processorThread = null;
    private Duration waitToIncomeMessageToProcess = Duration.ofSeconds(30);
    private int prefetchSize = 4;
    private AtomicLong rx_bytes = new AtomicLong(0);
    private AtomicLong rx = new AtomicLong(0);
    private Date idleTime;
    private AtomicLong tick = new AtomicLong(0);
    private String subscriberName;
    private RedisClientConfigProperties redisClientConfig;
    private StatefulConnection<String, byte[]> connect;
    private RedisMetricMonitor redisMetricMonitor;
    private TPS tps;
    private IRateLimiter rateLimiter;
    private boolean useZCommands = false;
    private final static byte[] wakeUpMessage = new byte[0];
    private static final String prefetchLPopScript = "local result = redis.call('lrange',KEYS[1],0,ARGV[1]-1)\n" +
            "redis.call('ltrim',KEYS[1],ARGV[1],-1)\n" +
            "return result";


    /**
     * Instantiates a new Redis message listener adaptor.
     *
     * @param redisMetricMonitor the redis metric monitor
     * @param subscriberName     the subscriber name
     * @param redisClientConfig  the redis client config
     * @param consumer           the consumer
     * @param queue              the queue
     * @param tps                the tps
     * @param rateLimiter        the rate limiter
     */
    public RedisMessageListenerAdaptor(RedisMetricMonitor redisMetricMonitor, String subscriberName, RedisClientConfigProperties redisClientConfig, IMsgConsumer consumer, String queue, TPS tps, IRateLimiter rateLimiter) {
        this.redisMetricMonitor = redisMetricMonitor;
        this.msgConsumer = consumer;
        this.queue = queue;
        this.subscriberName = subscriberName;
        this.redisClientConfig = redisClientConfig;
        this.tps = tps;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public String getSubscriberName() {
        return subscriberName;
    }

    @Override
    public Date getIdleTime() {
        return idleTime;
    }

    @Override
    public long getTick() {
        return tick.get();
    }

    @Override
    public String getQueueTps() {
        return tps.toString();
    }

    @Override
    public String getQueue() {
        return queue;
    }

    /**
     * Gets wait to income message to process.
     *
     * @return the wait to income message to process
     */
    public Duration getWaitToIncomeMessageToProcess() {
        return waitToIncomeMessageToProcess;
    }

    @Override
    public String getWaitToProcessString() {

        return waitToIncomeMessageToProcess.toString();
    }

    /**
     * Sets wait to income message to process.
     *
     * @param waitToIncomeMessageToProcess the wait to income message to process
     */
    public void setWaitToIncomeMessageToProcess(Duration waitToIncomeMessageToProcess) {
        this.waitToIncomeMessageToProcess = waitToIncomeMessageToProcess;
    }

    public int getPrefetchSize() {
        return prefetchSize;
    }

    /**
     * Sets prefetch size.
     *
     * @param prefetchSize the prefetch size
     */
    public void setPrefetchSize(int prefetchSize) {
        this.prefetchSize = prefetchSize;
    }

    @Override
    public String getType() {
        return "listener." + subscriberName;
    }

    @Override
    protected void startService() throws Throwable {
        subscribeMessage();

    }

    @Override
    public synchronized String start() {
        processorThread = new Thread(() -> RedisMessageListenerAdaptor.super.start(true));
        processorThread.start();
        return "StartAsync";
    }


    @Override
    protected void initService() throws Throwable {
        super.initService();
    }

    private void handleSingleMessage(RedisListener redisListener , RedisListCommands<String, byte[]>  listCommands) {
        checkConnection();
        byte[] rcvBytes = null;
        if (useZCommands) {
            KeyValue<String, ScoredValue<byte[]>> blpopObject = RedisCommandsUtil.getSortedSetCommands(connect).bzpopmin(waitToIncomeMessageToProcess.getSeconds(), queue);
            if (blpopObject != null)
                rcvBytes = blpopObject.getValue().getValue();
        } else {
            KeyValue<String, byte[]> blpopObject = listCommands.blpop(waitToIncomeMessageToProcess.getSeconds(), queue);
            if (blpopObject != null)
                rcvBytes = blpopObject.getValue();
        }

        if (rcvBytes != null) {
            if (logger.isDebugEnabled())
                logger.debug("redis message bytes from  qname={} byte length={}", queue, rcvBytes == null ? 0 : rcvBytes.length);
            rx.incrementAndGet();
            rx_bytes.addAndGet(rcvBytes.length);
            handleMessage(redisListener, rcvBytes);
        } else {
            if (logger.isWarnEnabled())
                logger.warn("redis subscriber is idle  session name={}", getClientSessionName());
            idleTime = new Date();
        }
    }

    public long getRxBytes() {
        return rx_bytes.get();
    }

    public long getRx() {
        return rx.get();
    }

    private boolean isWakeupMessage(byte[] bytes) {
        return bytes.length == 0;
    }


    private void handleMessage(RedisListener redisListener, byte[] bytes) {
        if (rateLimiter != null) {
            if (rateLimiter.tryAcquire("redis.listener."+getQueue()) == false) {
                logger.error("!!!! Rate Limiter Condition: ignore message  session name={} queuename={}", getClientSessionName(), queue);
            }
        }
        if (isWakeupMessage(bytes)) {
            if (logger.isWarnEnabled())
                logger.warn("redis subscriber receive empty message. maybe stop message  session name={}", getClientSessionName());
            return;
        }
        tps.tick();
        redisListener.handle(queue, bytes);
    }


    private void subscribeMessage() {
        Thread.currentThread().setName("redis-message-subscriber-" + queue);
        RedisListener redisListener = new RedisListener(msgConsumer);
        while (true) {
            try {

                connect = RedisCommandsUtil.connect(redisClientConfig);
                RedisUtil.setSessionInfo(connect, getClientSessionName());
                break;
            } catch (Exception ex) {
                if (logger.isErrorEnabled())
                    logger.error("error in connect redis. but continue after 2s", ex);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    /* nothing */
                }
            }
        }
        RedisScriptingCommands<String, byte[]> scriptingCommands = RedisCommandsUtil.getScriptingCommands(connect);
        RedisListCommands<String, byte[]> listCommands = RedisCommandsUtil.getListCommands(connect);
        String redis_ver = RedisUtil.getVersion(connect);
        boolean supportPrefetchPop = false;
        if (redis_ver != null) {
            int cv = RedisUtil.compareVersions(redis_ver, "6.2");
            supportPrefetchPop = (cv >= 0);
        }
        byte[] preFetchBytes = Integer.valueOf (prefetchSize).toString().getBytes();
        if (logger.isDebugEnabled())
            logger.debug("prefetch  bytes length={}", queue, preFetchBytes == null ? 0 : preFetchBytes.length);

        while (isRunning()) {
            try {

                tick.incrementAndGet();
                if (prefetchSize <= 1) {
                    handleSingleMessage(redisListener,listCommands);
                } else {
                    List<byte[]> list;
                    if (!useZCommands) {
                        if (!supportPrefetchPop)
                            list = scriptingCommands.eval(prefetchLPopScript, ScriptOutputType.MULTI, new String[]{queue}, preFetchBytes);
                        else
                            list = listCommands.lpop(queue, prefetchSize);
                    } else {
                        List<ScoredValue<byte[]>> zpopmin = RedisCommandsUtil.getSortedSetCommands(connect).zpopmin(queue, prefetchSize);
                        list = new ArrayList<>();
                        for (ScoredValue<byte[]> scoredValue : zpopmin) {
                            list.add(scoredValue.getValue());
                        }
                    }
                    if (list.isEmpty()) {
                        idleTime = new Date();
                        handleSingleMessage(redisListener,listCommands);
                    } else {
                        for (byte[] bytes : list) {
                            rx_bytes.addAndGet(bytes.length);
                        }
                        rx.addAndGet(list.size());
                        list.forEach(bytes -> {
                            handleMessage(redisListener, bytes);
                        });
                    }
                }
            } catch (Throwable e) {
                if (logger.isErrorEnabled())
                    logger.error("exception in  process session name={}", getClientSessionName(), e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    /* nothing */
                }
            } finally {
                // sync.clientSetname(getName().getBytes());
            }

        }
        if (logger.isWarnEnabled())
            logger.warn("stop redis listener on session name={}", getClientSessionName());

    }

    @Override
    protected void stopService() throws Throwable {
        if (!useZCommands)
            redisMetricMonitor.rpush(queue, wakeUpMessage);
        else
            redisMetricMonitor.zadd(queue, wakeUpMessage);
    }


    @Override
    public Long getPendingCount() {
        if (!useZCommands)
            return redisMetricMonitor.getPendingCount(queue);
        return 0L;
    }

    public void resetCounters() {
        rx_bytes.set(0);
        tick.set(0);
        rx.set(0);
        tps.reset();
    }


    public List<String> getLastMessage() {
        return redisMetricMonitor.getLastMessage(queue);
    }

    public List<String> getLastMessages(long start, long end) {
        return redisMetricMonitor.getLastMessages(queue, start, end);
    }

    @Override
    public long getSessionClientID() {
        return redisMetricMonitor.getClientID(getClientSessionName());
    }

    @Override
    public String getSessionInfo() {
        List<Map<String, String>> clientInfo = redisMetricMonitor.getClientInfo(getClientSessionName());
        if (clientInfo == null || clientInfo.size() == 0)
            return null;
        return clientInfo.get(0).toString();
    }

    @Override
    public String getClientSessionName() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        return "subscriber::" + getName() + ":" + jvmName;
    }

    private void checkConnection() {
        if (connect == null)
            throw new RuntimeException("redis connection not init for service");
    }

    @Override
    public String getBindRedisAddress() {
        return redisMetricMonitor.configGet("bind").toString();
    }

    @Override
    public String getBindRedisPort() {
        return redisMetricMonitor.configGet("port").toString();
    }

//    @Override
// unblock call by differnet connection
//    public String unBlockSession() {
//        try {
//            long clientId = getSessionClientID();
//            if (clientId == 0)
//                return "unblock fail client not found";
//            Long r = statefulRedisConnection.sync().clientUnblock(clientId, UnblockType.TIMEOUT);
//            return "unblock result=" + r;
//        } catch (Exception e) {
//            return e.getMessage();
//        }
//    }


}
