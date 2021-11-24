package io.avreen.common.mux;


import io.avreen.common.context.MsgContext;

import java.io.Serializable;


/**
 * The class Multiplexer async request.
 *
 * @param <T> the type parameter
 */
public class MultiplexerAsyncRequest<T> implements Serializable {
    private MsgContext<T> requestMsgContext;
    private Object handBack;
    private long sendMillis;

    /**
     * Instantiates a new Multiplexer async request.
     *
     * @param requestMsg the request msg
     * @param handBack   the hand back
     */
    public MultiplexerAsyncRequest(MsgContext<T> requestMsg, Object handBack , long sendMillis) {
        this.requestMsgContext = requestMsg;
        this.handBack = handBack;
        this.sendMillis = sendMillis;
    }

    /**
     * Instantiates a new Multiplexer async request.
     */
    public MultiplexerAsyncRequest() {
    }

    /**
     * Gets request msg.
     *
     * @return the request msg
     */
    public MsgContext<T> getRequestMsgContext() {
        return requestMsgContext;
    }

    /**
     * Gets hand back.
     *
     * @return the hand back
     */
    public Object getHandBack() {
        return handBack;
    }

    public long getSendMillis() {
        return sendMillis;
    }
}

