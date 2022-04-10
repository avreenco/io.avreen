package io.avreen.mq.api;

import io.avreen.common.context.MsgContext;

/**
 * The interface Msg publisher.
 *
 * @param <M> the type parameter
 */
public interface IMsgPublisher<M> {
    /**
     * Publish.
     *
     * @param queueName  the queue name
     * @param msgContext the msg context
     */
    void push(String queueName, MsgContext<M> msgContext);

}
