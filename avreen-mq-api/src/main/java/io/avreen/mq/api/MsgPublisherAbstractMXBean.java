package io.avreen.mq.api;

import io.avreen.common.actor.ActorBaseMXBean;

/**
 * The interface Msg publisher abstract mx bean.
 */
public interface MsgPublisherAbstractMXBean extends ActorBaseMXBean {
    /**
     * Gets total tx.
     *
     * @return the total tx
     */
    long getTotalTx();

    /**
     * Gets total tx pending.
     *
     * @return the total tx pending
     */
    long getTotalTxPending();

    /**
     * Reset statistics.
     */
    void resetStatistics();

    /**
     * Gets queue tx.
     *
     * @return the queue tx
     */
    String getQueueTx();

    /**
     * Gets queue tx pending.
     *
     * @return the queue tx pending
     */
    String getQueueTxPending();

}
