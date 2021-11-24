package io.avreen.common.netty.client;

import io.avreen.common.netty.NettyChannelBaseMXBean;

/**
 * The interface Netty client base mx bean.
 */
//@ManagedResource
public interface NettyClientBaseMXBean extends NettyChannelBaseMXBean {

    /**
     * Gets host.
     *
     * @return the host
     */
    String getHost();


    /**
     * Gets port.
     *
     * @return the port
     */
    int getPort();

    /**
     * Gets local ip.
     *
     * @return the local ip
     */
    String getLocalIP();

    /**
     * Gets local port.
     *
     * @return the local port
     */
    int getLocalPort();

    /**
     * Gets boss group number thread.
     *
     * @return the boss group number thread
     */
    int getBossGroupNumberThread();

}
