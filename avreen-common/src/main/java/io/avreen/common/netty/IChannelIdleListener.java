package io.avreen.common.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * The interface Channel idle listener.
 */
public interface IChannelIdleListener {
    /**
     * On channel idle.
     *
     * @param ctx the ctx
     */
    void onChannelIdle(ChannelHandlerContext ctx);
}

