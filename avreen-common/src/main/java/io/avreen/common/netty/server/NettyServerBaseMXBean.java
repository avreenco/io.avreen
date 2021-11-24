package io.avreen.common.netty.server;

// http example
//http://www.seepingmatter.com/2016/03/30/a-simple-standalone-http-server-with-netty.html

import io.avreen.common.netty.NettyChannelBaseMXBean;

/**
 * The interface Netty server base mx bean.
 */
public interface NettyServerBaseMXBean extends NettyChannelBaseMXBean {

    /**
     * Gets port.
     *
     * @return the port
     */
    int getPort();

    /**
     * Gets boss group number thread.
     *
     * @return the boss group number thread
     */
    int getBossGroupNumberThread();

    /**
     * Gets worker group number thread.
     *
     * @return the worker group number thread
     */
    int getWorkerGroupNumberThread();
}

