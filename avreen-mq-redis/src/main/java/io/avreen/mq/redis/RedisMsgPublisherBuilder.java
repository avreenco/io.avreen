package io.avreen.mq.redis;

import io.avreen.mq.api.IMsgPublisher;
import io.avreen.mq.api.MessageBrokerRegistry;
import io.avreen.redis.common.RedisClientConfigProperties;

import java.time.Duration;

/**
 * The class Redis msg publisher builder.
 *
 * @param <M> the type parameter
 */
public class RedisMsgPublisherBuilder<M> {
    private RedisClientConfigProperties redisClientConfig;
    private String name;
    private boolean publishAsync = true;
    private boolean bufferEnable = false;
    private int bufferQueueSize = 0;
    private Duration bufferOfferDuration = Duration.ofSeconds(1);
    private int bufferSubscribeHelperThreadCount = 2;

    /**
     * Instantiates a new Redis msg publisher builder.
     *
     * @param redisClientConfig the redis client config
     */
    public RedisMsgPublisherBuilder(RedisClientConfigProperties redisClientConfig) {
        this.redisClientConfig = redisClientConfig;
    }

    /**
     * Instantiates a new Redis msg publisher builder.
     */
    public RedisMsgPublisherBuilder() {
    }

    /**
     * Sets buffer enable.
     *
     * @param bufferEnable the buffer enable
     * @return the buffer enable
     */
    public RedisMsgPublisherBuilder<M> setBufferEnable(boolean bufferEnable) {
        this.bufferEnable = bufferEnable;
        return this;
    }

    /**
     * Sets buffer queue size.
     *
     * @param bufferQueueSize the buffer queue size
     * @return the buffer queue size
     */
    public RedisMsgPublisherBuilder<M> setBufferQueueSize(int bufferQueueSize) {
        this.bufferQueueSize = bufferQueueSize;
        return this;
    }

    /**
     * Sets buffer offer duration.
     *
     * @param bufferOfferDuration the buffer offer duration
     * @return the buffer offer duration
     */
    public RedisMsgPublisherBuilder<M> setBufferOfferDuration(Duration bufferOfferDuration) {
        this.bufferOfferDuration = bufferOfferDuration;
        return this;
    }

    /**
     * Sets buffer subscribe helper thread count.
     *
     * @param bufferSubscribeHelperThreadCount the buffer subscribe helper thread count
     * @return the buffer subscribe helper thread count
     */
    public RedisMsgPublisherBuilder<M> setBufferSubscribeHelperThreadCount(int bufferSubscribeHelperThreadCount) {
        this.bufferSubscribeHelperThreadCount = bufferSubscribeHelperThreadCount;
        return this;
    }

    /**
     * Is publish async boolean.
     *
     * @return the boolean
     */
    public boolean isPublishAsync() {
        return publishAsync;
    }

    /**
     * Sets publish async.
     *
     * @param publishAsync the publish async
     */
    public RedisMsgPublisherBuilder<M> setPublishAsync(boolean publishAsync) {
        this.publishAsync = publishAsync;
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
     * Build msg publisher.
     *
     * @return the msg publisher
     */
    public IMsgPublisher<M> build() {
        RedisPublisher<M> redisPublisher = new RedisPublisher<M>(redisClientConfig);
        if (getName() == null)
            setName("io/avreen/simulator/redis");
        redisPublisher.setPublishAsync(isPublishAsync());
        redisPublisher.setBufferEnable(bufferEnable);
        redisPublisher.setBufferOfferDuration(bufferOfferDuration);
        redisPublisher.setBufferSubscribeHelperThreadCount(bufferSubscribeHelperThreadCount);
        redisPublisher.setBufferQueueSize(bufferQueueSize);
        redisPublisher.setName(getName());
        MessageBrokerRegistry.registerPublisher(getName(), redisPublisher);
        redisPublisher.start();
        return redisPublisher;
    }
}
