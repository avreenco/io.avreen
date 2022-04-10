package io.avreen.mq.api;

import io.avreen.common.context.MsgContext;

/**
 * The interface Msg consumer.
 *
 * @param <M> the type parameter
 */
public interface IMsgConsumer<M> {
    /**
     * On msg.
     *
     * @param msgContext the msg
     */
    void onMsg(MsgContext<M> msgContext);


}
