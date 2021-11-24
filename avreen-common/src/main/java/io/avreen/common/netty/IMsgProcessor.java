package io.avreen.common.netty;

import io.avreen.common.context.MsgContext;
import io.netty.channel.ChannelHandlerContext;

/**
 * The interface Msg processor.
 *
 * @param <M> the type parameter
 */
public interface IMsgProcessor<M> {
    /**
     * Process.
     *
     * @param channelHandlerContext the channel handler context
     * @param msg                   the msg
     */
    void process(ChannelHandlerContext channelHandlerContext, MsgContext<M> msg);
}
