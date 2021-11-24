package io.avreen.common.netty;

import io.avreen.common.actor.ActorBaseMXBean;
import io.avreen.common.jmx.JmxDescriptionAnnotaion;

import java.util.List;
// http example
//http://www.seepingmatter.com/2016/03/30/a-simple-standalone-http-server-with-netty.html

/**
 * The interface Netty channel base mx bean.
 */
public interface NettyChannelBaseMXBean extends ActorBaseMXBean {
    /**
     * Reset counters.
     */
    void resetCounters();

    /**
     * Gets total active connections.
     *
     * @return the total active connections
     */
    @JmxDescriptionAnnotaion(value = "total connection that open and writable and ready to send and receive")
    int getTotalActiveConnections();

    /**
     * Gets total connections.
     *
     * @return the total connections
     */
    @JmxDescriptionAnnotaion(value = "total connection that established.")
    int getTotalConnections();

    /**
     * Gets message expire second.
     *
     * @return the message expire second
     */
    int getMessageExpireSecond();

    /**
     * Gets sessions.
     *
     * @return the sessions
     */
    String getSessions();


    /**
     * Gets rx.
     *
     * @return the rx
     */
    long getRx();

    /**
     * Gets rx pending.
     *
     * @return the rx pending
     */
    int getRx_pending();

    /**
     * Gets tx.
     *
     * @return the tx
     */
    long getTx();

    /**
     * Gets tx pending.
     *
     * @return the tx pending
     */
    int getTx_pending();

    /**
     * Gets decode failed.
     *
     * @return the decode failed
     */
    int getDecodeFailed();

    /**
     * Gets encode failed.
     *
     * @return the encode failed
     */
    int getEncodeFailed();

    /**
     * Is enable ssl boolean.
     *
     * @return the boolean
     */
    boolean isEnableSSL();


    /**
     * Is registered session event handler boolean.
     *
     * @return the boolean
     */
    boolean isRegisteredSessionEventHandler();

    /**
     * Gets allows ip.
     *
     * @return the allows ip
     */
    List<String> getAllowsIp();

    /**
     * Gets denies ip.
     *
     * @return the denies ip
     */
    List<String> getDeniesIp();

    /**
     * Gets transport type.
     *
     * @return the transport type
     */
    TransportTypes getTransportType();

    /**
     * Any active connected boolean.
     *
     * @return the boolean
     */
    boolean anyActiveConnected();

    /**
     * Gets options string.
     *
     * @return the options string
     */
    String getOptionsString();

    /**
     * Gets traffic info.
     *
     * @return the traffic info
     */
    String getTrafficInfo();

    /**
     * Gets idle time string.
     *
     * @return the idle time string
     */
    String getIdleTimeString();

    /**
     * Gets read timeout string.
     *
     * @return the read timeout string
     */
    String getReadTimeoutString();

    /**
     * Gets rx tps.
     *
     * @return the rx tps
     */
    String getRxTps();

    /**
     * Gets tx tps.
     *
     * @return the tx tps
     */
    String getTxTps();

    /**
     * Gets rx process failed tps.
     *
     * @return the rx process failed tps
     */
    String getRxProcessFailedTps();

    /**
     * Gets decode rate limiter info.
     *
     * @return the decode rate limiter info
     */
    String getDecodeRateLimiterInfo();

    /**
     * Is close channel on decode rate condition boolean.
     *
     * @return the boolean
     */
    boolean isCloseChannelOnDecodeRateCondition();
}

