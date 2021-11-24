package io.avreen.iso8583.channel.tcp;


import io.avreen.common.netty.client.NettyClientBaseMXBean;

/**
 * The interface Iso netty client mx bean.
 */
public interface ISONettyClientMXBean extends NettyClientBaseMXBean {

    /**
     * Gets channel props.
     *
     * @return the channel props
     */
    String getChannelProps();

}
