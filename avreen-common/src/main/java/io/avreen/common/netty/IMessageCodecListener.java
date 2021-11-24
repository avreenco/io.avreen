package io.avreen.common.netty;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * The interface Message codec listener.
 *
 * @param <M> the type parameter
 */
public interface IMessageCodecListener<M> {
    /**
     * Before decode.
     *
     * @param channelHandlerContext the channel handler context
     */
    void beforeDecode(ChannelHandlerContext channelHandlerContext);

    /**
     * After decode.
     *
     * @param channelHandlerContext the channel handler context
     * @param channelID             the channel id
     * @param m                     the m
     * @param e                     the e
     * @param additionalInfo        the additional info
     */
    void afterDecode(ChannelHandlerContext channelHandlerContext, String channelID, M m, Throwable e, Map<String, Object> additionalInfo);

    /**
     * Before encode.
     *
     * @param channelHandlerContext the channel handler context
     * @param m                     the m
     */
    void beforeEncode(ChannelHandlerContext channelHandlerContext, M m);

    /**
     * After encode.
     *
     * @param channelHandlerContext the channel handler context
     * @param m                     the m
     * @param e                     the e
     */
    void afterEncode(ChannelHandlerContext channelHandlerContext, M m, Throwable e);

}
