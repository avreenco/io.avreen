package io.avreen.mq.local;

import io.avreen.common.actor.ActorBaseMXBean;

import java.util.Date;

/**
 * The interface Space base msg listener adaptor mx bean.
 *
 * @param <T> the type parameter
 */
public interface LocalQListenerAdaptorMXBean<T> extends ActorBaseMXBean {

    /**
     * Gets queues capacity.
     *
     * @return the queues capacity
     */
    int getQueuesCapacity();

    /**
     * Gets space uri.
     *
     * @return the space uri
     */
    String getSpaceURI();

    /**
     * Gets queue name.
     *
     * @return the queue name
     */
    String getQueueName();

    /**
     * Gets wait to process string.
     *
     * @return the wait to process string
     */
    String getWaitToProcessString();

    /**
     * Gets rx.
     *
     * @return the rx
     */
    long getRx();

    /**
     * Gets idle time.
     *
     * @return the idle time
     */
    Date getIdleTime();

    /**
     * Gets tick.
     *
     * @return the tick
     */
    long getTick();

    /**
     * Reset counters.
     */
    void resetCounters();

    /**
     * Gets subscriber name.
     *
     * @return the subscriber name
     */
    String getSubscriberName();

    /**
     * Gets queue tps.
     *
     * @return the queue tps
     */
    String getQueueTps();


}
