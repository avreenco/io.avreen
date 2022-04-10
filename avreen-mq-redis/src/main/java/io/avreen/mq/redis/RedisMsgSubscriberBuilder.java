package io.avreen.mq.redis;

import io.avreen.common.limiter.IRateLimiter;
import io.avreen.mq.api.IMsgSubscriber;
import io.avreen.mq.api.MessageBrokerRegistry;
import io.avreen.redis.common.RedisClientConfigProperties;

import java.time.Duration;

/**
 * The class Redis msg subscriber builder.
 *
 * @param <M> the type parameter
 */
public class RedisMsgSubscriberBuilder<M> {
    private int consumerCount = 1;
    private Duration waitToIncomeMessageToProcess = Duration.ofSeconds(30);
    private String name;
    private int prefetchSize = 4;
    private RedisClientConfigProperties redisClientConfig;
    private SubscriberMode subscriberMode = SubscriberMode.Active_Active;
    private IRateLimiter rateLimiter;

    /**
     * Instantiates a new Redis msg subscriber builder.
     *
     * @param redisClientConfig the redis client config
     * @param consumerCount     the consumer count
     */
    public RedisMsgSubscriberBuilder(RedisClientConfigProperties redisClientConfig, int consumerCount) {
        this.redisClientConfig = redisClientConfig;
        this.consumerCount = consumerCount;
    }

    /**
     * Instantiates a new Redis msg subscriber builder.
     */
    public RedisMsgSubscriberBuilder() {
    }

    /**
     * Gets prefetch size.
     *
     * @return the prefetch size
     */
    public int getPrefetchSize() {
        return prefetchSize;
    }

    /**
     * Sets prefetch size.
     *
     * @param prefetchSize the prefetch size
     * @return the prefetch size
     */
    public RedisMsgSubscriberBuilder<M> setPrefetchSize(int prefetchSize) {
        this.prefetchSize = prefetchSize;
        return this;
    }

    /**
     * Sets subscriber mode.
     *
     * @param subscriberMode the subscriber mode
     * @return the subscriber mode
     */
    public RedisMsgSubscriberBuilder<M> setSubscriberMode(SubscriberMode subscriberMode) {
        this.subscriberMode = subscriberMode;
        return this;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets consumer count.
     *
     * @return the consumer count
     */
    public int getConsumerCount() {
        return consumerCount;
    }

    /**
     * Sets consumer count.
     *
     * @param consumerCount the consumer count
     * @return the consumer count
     */
    public RedisMsgSubscriberBuilder<M> setConsumerCount(int consumerCount) {
        this.consumerCount = consumerCount;
        return this;
    }

    /**
     * Gets wait to income message to process.
     *
     * @return the wait to income message to process
     */
    public Duration getWaitToIncomeMessageToProcess() {
        return waitToIncomeMessageToProcess;
    }

    /**
     * Sets wait to income message to process.
     *
     * @param waitToIncomeMessageToProcess the wait to income message to process
     * @return the wait to income message to process
     */
    public RedisMsgSubscriberBuilder<M> setWaitToIncomeMessageToProcess(Duration waitToIncomeMessageToProcess) {
        this.waitToIncomeMessageToProcess = waitToIncomeMessageToProcess;
        return this;
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
     * @return the rate limiter
     */
    public RedisMsgSubscriberBuilder<M> setRateLimiter(IRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    /**
     * Build msg subscriber.
     *
     * @return the msg subscriber
     */
    public IMsgSubscriber<M> build() {
        if (getName() == null)
            setName("io/avreen/simulator/redis");

        RedisSubscriber<M> redisSubscriber = new RedisSubscriber(redisClientConfig, consumerCount);
        redisSubscriber.setName(getName());
        redisSubscriber.setWaitToIncomeMessageToProcess(waitToIncomeMessageToProcess);
        redisSubscriber.setPrefetchSize(prefetchSize);
        redisSubscriber.setRateLimiter(rateLimiter);
        redisSubscriber.setSubscriberMode(subscriberMode);
        MessageBrokerRegistry.registerSubscriber(getName(), redisSubscriber);
        redisSubscriber.start();
        return redisSubscriber;

    }
}
