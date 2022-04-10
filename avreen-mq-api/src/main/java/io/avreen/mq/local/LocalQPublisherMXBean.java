package io.avreen.mq.local;


import io.avreen.mq.api.MsgPublisherAbstractMXBean;

/**
 * The interface Space base msg publisher mx bean.
 */
public interface LocalQPublisherMXBean extends MsgPublisherAbstractMXBean {
    /**
     * Gets offer timeout string.
     *
     * @return the offer timeout string
     */
    String getOfferTimeoutString();

    /**
     * Gets space name.
     *
     * @return the space name
     */
    String getSpaceName();

    /**
     * Gets queues capacity.
     *
     * @return the queues capacity
     */
    int getQueuesCapacity();

    /**
     * Gets queues size.
     *
     * @return the queues size
     */
    int getQueuesSize();

    /**
     * Gets tps.
     *
     * @return the tps
     */
    String getTps();


}
