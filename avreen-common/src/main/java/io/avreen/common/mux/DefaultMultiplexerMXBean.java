package io.avreen.common.mux;


import io.avreen.common.actor.ActorBaseMXBean;

/**
 * The interface Default multiplexer mx bean.
 */
public interface DefaultMultiplexerMXBean extends ActorBaseMXBean {
    /**
     * Reset counters.
     */
    void resetCounters();

    /**
     * Gets rx.
     *
     * @return the rx
     */
    long getRx();

    /**
     * Gets tx.
     *
     * @return the tx
     */
    long getTx();

    /**
     * Gets rx expired.
     *
     * @return the rx expired
     */
    long getRx_expired();

    /**
     * Gets rx pending.
     *
     * @return the rx pending
     */
    int getRx_pending();

    /**
     * Gets rx unhandled.
     *
     * @return the rx unhandled
     */
    int getRx_unhandled();


    /**
     * Gets pending limit.
     *
     * @return the pending limit
     */
    int getPendingLimit();

    /**
     * Gets request tps.
     *
     * @return the request tps
     */
    String getRequestTPS();

    /**
     * Gets response tps.
     *
     * @return the response tps
     */
    String getResponseTPS();


}
