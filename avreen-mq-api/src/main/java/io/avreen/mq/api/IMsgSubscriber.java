package io.avreen.mq.api;

/**
 * The interface Msg subscriber.
 *
 * @param <M> the type parameter
 */
public interface IMsgSubscriber<M> {
    /**
     * Subscribe.
     *
     * @param queueName the queue name
     * @param consumer  the consumer
     */
    void subscribe(String queueName, IMsgConsumer<M> consumer);

    /**
     * Unsubscribe.
     *
     * @param queueName the queue name
     */
    void unsubscribe(String queueName);

}
