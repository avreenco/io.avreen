package io.avreen.common.mux;


import io.avreen.common.context.MsgContext;

/**
 * The interface Multiplexer.
 *
 * @param <T> the type parameter
 */
public interface IMultiplexer<T> {
    /**
     * The constant MUX_TYPE.
     */
    String MUX_TYPE = "mux";

    /**
     * Request t.
     *
     * @param msg     the msg
     * @param timeout the timeout
     * @return the t
     * @throws MultiplexerException the multiplexer exception
     */
    T request(MsgContext<T> msg, long timeout) throws MultiplexerException;

    /**
     * Request async.
     *
     * @param msg      the msg
     * @param timeout  the timeout
     * @param handBack the hand back
     * @throws MultiplexerException the multiplexer exception
     */
    void requestAsync(MsgContext<T> msg, long timeout, Object handBack) throws MultiplexerException;


}
