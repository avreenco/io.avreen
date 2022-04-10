package io.avreen.mq.redis;

import io.avreen.common.limiter.IRateLimiter;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.TPS;
import io.avreen.mq.api.IMsgConsumer;
import io.avreen.mq.api.MsgSubscriberAbstract;
import io.avreen.redis.common.RedisClientConfigProperties;
import io.avreen.redis.common.RedisLockHelper;
import io.avreen.redis.common.RedisMetricMonitor;
import io.lettuce.core.api.StatefulConnection;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The class Redis subscriber.
 *
 * @param <M> the type parameter
 */
public class RedisSubscriber<M> extends MsgSubscriberAbstract<M> implements RedisSubscriberMXBean {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".reactor.messagebroker.impl.redis.RedisSubscriber");
    private Duration waitToIncomeMessageToProcess = Duration.ofSeconds(30);
    private int consumerCount;
    private int prefetchSize = 4;
    private RedisClientConfigProperties redisClientConfig;
    private RedisMetricMonitor redisMetricMonitor;
    private SubscriberMode subscriberMode;
    private Set<String> subscriberQueues = new HashSet<>();
    private IRateLimiter rateLimiter = null;


    /**
     * Instantiates a new Redis subscriber.
     *
     * @param redisClientConfig the redis client config
     * @param consumerCount     the consumer count
     */
    public RedisSubscriber(RedisClientConfigProperties redisClientConfig, int consumerCount) {
        this.redisClientConfig = redisClientConfig;
        this.consumerCount = consumerCount;
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


    private HashMap<String, List<RedisMessageListenerAdaptor>> adaptorMap = new HashMap<>();

    /**
     * Gets subscriber mode.
     *
     * @return the subscriber mode
     */
    public SubscriberMode getSubscriberMode() {
        return subscriberMode;
    }

    /**
     * Sets subscriber mode.
     *
     * @param subscriberMode the subscriber mode
     */
    public void setSubscriberMode(SubscriberMode subscriberMode) {
        this.subscriberMode = subscriberMode;
    }

    private String getActivePassiveControlKey(String queueName) {
        return "active-passive-key." + getName() + "." + queueName;
    }

    private byte[] getActivePassiveControlValue(String queueName) {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        return jvmName.getBytes();
    }


    @Override
    public void subscribeMsg(String queueName, IMsgConsumer<M> consumer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                redisMetricMonitor = new RedisMetricMonitor("subscriber." + getName(), redisClientConfig);
                if (SubscriberMode.Active_Passive.equals(subscriberMode)) {
                    StatefulConnection<String, byte[]> stringStatefulConnection = redisMetricMonitor.checkConnection();
                    RedisLockHelper redisLockHelper = new RedisLockHelper(stringStatefulConnection, "redisSub." + getName() + "." + queueName);
                    redisLockHelper.lock();
                }
                subscriberQueues.add(queueName);
                TPS tps = new TPS();
                for (int idx = 0; idx < consumerCount; idx++) {
                    RedisMessageListenerAdaptor redisMessageListenerAdaptor = new RedisMessageListenerAdaptor(redisMetricMonitor, getName(), redisClientConfig, consumer, queueName, tps, rateLimiter);
                    redisMessageListenerAdaptor.setWaitToIncomeMessageToProcess(waitToIncomeMessageToProcess);
                    redisMessageListenerAdaptor.setPrefetchSize(prefetchSize);
                    redisMessageListenerAdaptor.setName(queueName + "-consumer-" + (idx + 1));
                    redisMessageListenerAdaptor.start();

                    List<RedisMessageListenerAdaptor> adaptorList = adaptorMap.get(queueName);
                    if (adaptorList == null)
                        adaptorList = new ArrayList<>();
                    adaptorList.add(redisMessageListenerAdaptor);
                    adaptorMap.putIfAbsent(queueName, adaptorList);
                }
            }
        }).start();
    }


    @Override
    public String getType() {
        return "RedisSubscriber";
    }

    @Override
    public int getConsumerCount() {
        return consumerCount;
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

    public Set<String> getSubscriberQueues() {
        return subscriberQueues;
    }

    @Override
    public String getRateLimiterName() {
        if (rateLimiter == null)
            return "";
        return rateLimiter.getName();

    }

    @Override
    public String getBindRedisAddress() {
        return redisMetricMonitor.configGet("bind").toString();
    }

    @Override
    public String getBindRedisPort() {
        return redisMetricMonitor.configGet("port").toString();
    }

    public Long getPendingCount(String queue) {
        return redisMetricMonitor.getPendingCount(queue);
    }

    /**
     * Gets rate limiter.
     *
     * @return the rate limiter
     */
    public IRateLimiter getRateLimiter() {
        return rateLimiter;
    }

    /**
     * Sets rate limiter.
     *
     * @param rateLimiter the rate limiter
     */
    public void setRateLimiter(IRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public List<String> getLastMessage(String queue) {
        return redisMetricMonitor.getLastMessage(queue);

    }

    public List<String> getLastMessages(String queue, long start, long end) {
        return redisMetricMonitor.getLastMessages(queue, start, end);
    }

    @Override
    public String ping() {
        return redisMetricMonitor.ping();
    }

    @Override
    public String executeCommand(String fullCommand) {
        return redisMetricMonitor.executeCommand(fullCommand);
    }


    @Override
    public void unsubscribe(String queueName) {
        List<RedisMessageListenerAdaptor> redisMessageListenerAdaptors = adaptorMap.get(queueName);
        if (redisMessageListenerAdaptors == null)
            return;
        redisMessageListenerAdaptors.forEach(redisMessageListenerAdaptor -> {
            redisMessageListenerAdaptor.stop();
        });
    }

    @Override
    protected void stopService() throws Throwable {
        super.stopService();
        if (adaptorMap == null)
            return;
        adaptorMap.forEach((s, redisMessageListenerAdaptors) -> unsubscribe(s));
    }

    @Override
    protected void startService() throws Throwable {
        super.startService();
        if (adaptorMap == null)
            return;
        adaptorMap.forEach(new BiConsumer<String, List<RedisMessageListenerAdaptor>>() {
            @Override
            public void accept(String s, List<RedisMessageListenerAdaptor> redisMessageListenerAdaptors) {
                if (redisMessageListenerAdaptors != null)
                    redisMessageListenerAdaptors.forEach(new Consumer<RedisMessageListenerAdaptor>() {
                        @Override
                        public void accept(RedisMessageListenerAdaptor redisMessageListenerAdaptor) {
                            redisMessageListenerAdaptor.start();
                        }
                    });
            }
        });
    }
}
