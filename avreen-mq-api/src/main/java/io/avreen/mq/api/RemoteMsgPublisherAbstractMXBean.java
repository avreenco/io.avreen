package io.avreen.mq.api;

/**
 * The interface Remote msg publisher abstract mx bean.
 */
public interface RemoteMsgPublisherAbstractMXBean extends MsgPublisherAbstractMXBean {
    /**
     * Is buffer enable boolean.
     *
     * @return the boolean
     */
    boolean isBufferEnable();

    /**
     * Gets buffer queue size.
     *
     * @return the buffer queue size
     */
    int getBufferQueueSize();

    /**
     * Gets buffer offer duration string.
     *
     * @return the buffer offer duration string
     */
    String getBufferOfferDurationString();

    /**
     * Gets buffer subscribe helper thread count.
     *
     * @return the buffer subscribe helper thread count
     */
    int getBufferSubscribeHelperThreadCount();
}
