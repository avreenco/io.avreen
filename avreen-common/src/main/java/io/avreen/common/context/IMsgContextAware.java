package io.avreen.common.context;

/**
 * The interface Msg context aware.
 *
 * @param <M> the type parameter
 */
public interface IMsgContextAware<M> {
    /**
     * Sets msg context.
     *
     * @param msgContext the msg context
     */
    void setMsgContext(MsgContext<M> msgContext);

    /**
     * Gets msg context.
     *
     * @return the msg context
     */
    MsgContext<M> getMsgContext();
}
