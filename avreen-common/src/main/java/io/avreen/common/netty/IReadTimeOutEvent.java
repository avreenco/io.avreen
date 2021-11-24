package io.avreen.common.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * The interface Read time out event.
 */
public interface IReadTimeOutEvent {
    /**
     * Read timed out.
     *
     * @param ctx the ctx
     */
    void readTimedOut(ChannelHandlerContext ctx);
}
