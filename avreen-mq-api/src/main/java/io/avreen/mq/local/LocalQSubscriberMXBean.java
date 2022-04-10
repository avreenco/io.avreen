package io.avreen.mq.local;


import io.avreen.mq.api.MsgSubscriberAbstractMXBean;

/**
 * The interface Space base msg subscriber mx bean.
 */
public interface LocalQSubscriberMXBean extends MsgSubscriberAbstractMXBean {
    /**
     * Gets consumer count.
     *
     * @return the consumer count
     */
    int getConsumerCount();

    /**
     * Gets wait to process string.
     *
     * @return the wait to process string
     */
    String getWaitToProcessString();

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


}
