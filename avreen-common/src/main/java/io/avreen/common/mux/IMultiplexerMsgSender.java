package io.avreen.common.mux;

import io.avreen.common.context.MsgContext;

/**
 * The interface Multiplexer msg sender.
 *
 * @param <T> the type parameter
 */
public interface IMultiplexerMsgSender<T> {
    /**
     * Send message.
     *
     * @param msg     the msg
     * @param timeout the timeout
     */
    void sendMessage(MsgContext<T> msg, long timeout);
}
