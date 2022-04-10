package io.avreen.mq.local;

import io.avreen.common.context.MsgContext;
import io.avreen.common.util.TPS;
import io.avreen.mq.api.MsgPublisherAbstract;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * The class Space base msg publisher.
 *
 * @param <M> the type parameter
 */
class LocalQPublisher<M> extends MsgPublisherAbstract<M> implements LocalQPublisherMXBean {
    private LocalQ space;
    private Duration offerTimeout = Duration.ofSeconds(2);

    private TPS tps = new TPS();

    /**
     * Instantiates a new Space base msg publisher.
     *
     * @param space the space
     */
    public LocalQPublisher(LocalQ space) {
        this.space = space;
    }

    /**
     * Gets offer timeout.
     *
     * @return the offer timeout
     */
    public Duration getOfferTimeout() {
        return offerTimeout;
    }

    public String getOfferTimeoutString() {
        if (offerTimeout == null)
            return "-";
        return offerTimeout.toString();
    }

    /**
     * Sets offer timeout.
     *
     * @param offerTimeout the offer timeout
     */
    public void setOfferTimeout(Duration offerTimeout) {
        this.offerTimeout = offerTimeout;
    }

    public String getSpaceName() {
        return space.getName();
    }

    public int getQueuesCapacity() {
        return space.getQueuesCapacity();
    }

    public int getQueuesSize() {
        return space.getQueuesSize();
    }

    @Override
    public String getTps() {
        return tps.toString();
    }

    @Override
    public void publishMsg(String queueName, MsgContext<M> msgContext) {
        try {
            boolean offered = space.offer(queueName, msgContext, offerTimeout.getSeconds(), TimeUnit.SECONDS);
            if (!offered)
                throw new RuntimeException("queue offer time out. may be queue full. offerTimeout=" + offerTimeout);
            tps.tick();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String getType() {
        return "InMemoryPublisher";
    }
}
