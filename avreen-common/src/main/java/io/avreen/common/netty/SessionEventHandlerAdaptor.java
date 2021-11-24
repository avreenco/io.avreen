package io.avreen.common.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

/**
 * The class Session event handler adaptor.
 */
@ChannelHandler.Sharable
public class SessionEventHandlerAdaptor extends ChannelDuplexHandler {
    private ISessionEventListener sessionEvent;

    /**
     * Instantiates a new Session event handler adaptor.
     *
     * @param sessionEvent the session event
     */
    public SessionEventHandlerAdaptor(ISessionEventListener sessionEvent) {
        this.sessionEvent = sessionEvent;
    }

    /**
     * Instantiates a new Session event handler adaptor.
     */
    public SessionEventHandlerAdaptor() {
    }

    /**
     * Sets session event.
     *
     * @param sessionEvent the session event
     * @return the session event
     */
    public SessionEventHandlerAdaptor setSessionEvent(ISessionEventListener sessionEvent) {
        this.sessionEvent = sessionEvent;
        return this;
    }


//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        super.handlerAdded(ctx);
//        if (sessionEventListener != null)
//            sessionEventListener.fire("handlerAdded", ctx.channel(), null);
//    }
//
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//        super.handlerRemoved(ctx);
//        if (sessionEventListener != null)
//            sessionEventListener.fire("handlerRemoved", ctx.channel(), null);
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.channelActive, ctx.channel().remoteAddress(), ctx.channel().localAddress(), null);

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.channelInactive, ctx.channel(), null);

    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.connect(ctx, remoteAddress, localAddress, promise);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.connect, remoteAddress, localAddress, null);

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.channelRegistered, ctx.channel(), null);

    }

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.bind(ctx, localAddress, promise);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.bind, null, localAddress, null);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.disconnect(ctx, promise);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.disconnect, ctx.channel(), null);

    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.close, ctx.channel(), null);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.deregister(ctx, promise);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.deregister, ctx.channel(), null);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.channelUnregistered, ctx.channel(), null);

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        if (sessionEvent != null)
            sessionEvent.fire(ChannelEventTypes.exceptionCaught, ctx.channel(), cause);

    }

}
