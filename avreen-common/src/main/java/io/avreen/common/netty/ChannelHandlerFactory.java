package io.avreen.common.netty;

import io.netty.channel.ChannelHandler;

/**
 * The interface Channel handler factory.
 */
public interface ChannelHandlerFactory {
    /**
     * Create channel handler.
     *
     * @return the channel handler
     */
    ChannelHandler create();
}
