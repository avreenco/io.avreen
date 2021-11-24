
package io.avreen.iso8583.channel.tcp;


import io.avreen.common.netty.server.NettyServerBaseMXBean;

/**
 * The interface Iso netty server mx bean.
 */
public interface ISONettyServerMXBean extends NettyServerBaseMXBean {
    /**
     * Gets channel props.
     *
     * @return the channel props
     */
    String getChannelProps();


}

