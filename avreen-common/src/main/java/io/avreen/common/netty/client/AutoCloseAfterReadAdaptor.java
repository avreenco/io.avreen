package io.avreen.common.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * The class Auto close after read adaptor.
 */
public class AutoCloseAfterReadAdaptor extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        AttributeKey<Boolean> closeManual = AttributeKey.valueOf("normalClose");
        ctx.channel().attr(closeManual);
        ctx.close();
    }
}
