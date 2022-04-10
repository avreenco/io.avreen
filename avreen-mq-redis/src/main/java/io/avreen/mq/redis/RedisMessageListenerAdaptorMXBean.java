package io.avreen.mq.redis;

import java.util.Date;
import java.util.List;

/**
 * The interface Redis message listener adaptor mx bean.
 */
public interface RedisMessageListenerAdaptorMXBean extends RedisMessageBrokerComponentMXBean {
    /**
     * Gets queue.
     *
     * @return the queue
     */
    String getQueue();

    /**
     * Gets wait to process string.
     *
     * @return the wait to process string
     */
    String getWaitToProcessString();

    /**
     * Gets prefetch size.
     *
     * @return the prefetch size
     */
    int getPrefetchSize();

    /**
     * Gets rx bytes.
     *
     * @return the rx bytes
     */
    long getRxBytes();

    /**
     * Gets rx.
     *
     * @return the rx
     */
    long getRx();

    /**
     * Gets tick.
     *
     * @return the tick
     */
    long getTick();

    /**
     * Gets queue tps.
     *
     * @return the queue tps
     */
    String getQueueTps();

    /**
     * Gets idle time.
     *
     * @return the idle time
     */
    Date getIdleTime();

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
     * Gets pending count.
     *
     * @return the pending count
     */
    Long getPendingCount();

    /**
     * Gets last message.
     *
     * @return the last message
     */
    List<String> getLastMessage();

    /**
     * Gets last messages.
     *
     * @param start the start
     * @param end   the end
     * @return the last messages
     */
    List<String> getLastMessages(long start, long end);


}
