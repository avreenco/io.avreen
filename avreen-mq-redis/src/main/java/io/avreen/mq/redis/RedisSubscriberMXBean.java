package io.avreen.mq.redis;


import io.avreen.mq.api.MsgSubscriberAbstractMXBean;

import java.util.List;
import java.util.Set;

/**
 * The interface Redis subscriber mx bean.
 *
 * @param <M> the type parameter
 */
public interface RedisSubscriberMXBean<M> extends MsgSubscriberAbstractMXBean {
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
     * Gets prefetch size.
     *
     * @return the prefetch size
     */
    int getPrefetchSize();

    /**
     * Gets bind redis address.
     *
     * @return the bind redis address
     */
    String getBindRedisAddress();

    /**
     * Gets bind redis port.
     *
     * @return the bind redis port
     */
    String getBindRedisPort();

    /**
     * Gets pending count.
     *
     * @param queue the queue
     * @return the pending count
     */
    Long getPendingCount(String queue);

    /**
     * Gets last message.
     *
     * @param queue the queue
     * @return the last message
     */
    List<String> getLastMessage(String queue);

    /**
     * Gets last messages.
     *
     * @param queue the queue
     * @param start the start
     * @param end   the end
     * @return the last messages
     */
    List<String> getLastMessages(String queue, long start, long end);

    /**
     * Ping string.
     *
     * @return the string
     */
    String ping();

    /**
     * Execute command string.
     *
     * @param fullCommand the full command
     * @return the string
     */
    String executeCommand(String fullCommand);

    /**
     * Gets subscriber queues.
     *
     * @return the subscriber queues
     */
    Set<String> getSubscriberQueues();

    /**
     * Gets rate limiter name.
     *
     * @return the rate limiter name
     */
    String getRateLimiterName();


}
