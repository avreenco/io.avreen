package io.avreen.common.netty;

import io.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * The interface Session event listener.
 */
public interface ISessionEventListener {
    /**
     * Fire.
     *
     * @param eventName the event name
     * @param channel   the channel
     * @param e         the e
     */
    void fire(ChannelEventTypes eventName, Channel channel, Throwable e);

    /**
     * Fire.
     *
     * @param eventName     the event name
     * @param remoteAddress the remote address
     * @param localAddress  the local address
     * @param e             the e
     */
    void fire(ChannelEventTypes eventName, SocketAddress remoteAddress, SocketAddress localAddress, Throwable e);

}

