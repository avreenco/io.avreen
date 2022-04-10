package io.avreen.mq.redis;

import io.avreen.common.jmx.JmxDescriptionAnnotaion;
import io.avreen.common.jmx.JmxOperationParamAnnotaion;
import io.avreen.mq.api.RemoteMsgPublisherAbstractMXBean;

import java.util.List;

/**
 * The interface Redis publisher mx bean.
 */
public interface RedisPublisherMXBean extends RemoteMsgPublisherAbstractMXBean, RedisMessageBrokerComponentMXBean {
    /**
     * Is publish async boolean.
     *
     * @return the boolean
     */
    boolean isPublishAsync();

    /**
     * Sets publish async.
     *
     * @param publishAsync the publish async
     */
    void setPublishAsync(boolean publishAsync);

    /**
     * Gets total tx bytes.
     *
     * @return the total tx bytes
     */
    @JmxDescriptionAnnotaion("publish bytes to all queues")
    long getTotalTxBytes();

    /**
     * Gets queue tx bytes.
     *
     * @return the queue tx bytes
     */
    @JmxDescriptionAnnotaion("publish bytes to per queue")
    String getQueueTxBytes();


    /**
     * Gets pending count.
     *
     * @param queue the queue
     * @return the pending count
     */
    @JmxDescriptionAnnotaion("pending consume count  per queue")
    Long getPendingCount(@JmxOperationParamAnnotaion("queueName") String queue);

    /**
     * Gets last message.
     *
     * @param queue the queue
     * @return the last message
     */
    List<String> getLastMessage(@JmxOperationParamAnnotaion("queueName") String queue);

    /**
     * Gets last messages.
     *
     * @param queue the queue
     * @param start the start
     * @param end   the end
     * @return the last messages
     */
    List<String> getLastMessages(@JmxOperationParamAnnotaion("queueName") String queue, @JmxOperationParamAnnotaion("startIndex") long start, @JmxOperationParamAnnotaion("endIndex") long end);

    /**
     * Ping string.
     *
     * @return the string
     */
    @JmxDescriptionAnnotaion("ping method result must be PONG")
    String ping();

    @JmxDescriptionAnnotaion("redis client id")
    long getSessionClientID();

    String getSessionInfo();

    @JmxDescriptionAnnotaion("redis client name")
    String getClientSessionName();

    /**
     * Execute command string.
     *
     * @param fullCommand the full command
     * @return the string
     */
    @JmxDescriptionAnnotaion("execute redis command")
    String executeCommand(@JmxDescriptionAnnotaion("full redis command text") @JmxOperationParamAnnotaion("fullCommand") String fullCommand);

    /**
     * Gets tps.
     *
     * @return the tps
     */
    @JmxDescriptionAnnotaion("calculate transaction per second")
    String getTps();


}
