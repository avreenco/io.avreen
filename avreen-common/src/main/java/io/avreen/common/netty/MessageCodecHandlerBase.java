package io.avreen.common.netty;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.context.ContextKeyUtil;
import io.avreen.common.context.IMsgContextAware;
import io.avreen.common.context.MsgContext;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.log.LoggerDomain;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The class Message codec handler base.
 *
 * @param <T> the type parameter
 */
public abstract class MessageCodecHandlerBase<T> extends ByteToMessageCodec<T> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.MessageCodecHandlerBase");
    private IMessageCodecListener<T> messageCodecListener;
    private String channelID;

    /**
     * Instantiates a new Message codec handler base.
     *
     * @param channelID            the channel id
     * @param messageCodecListener the message codec listener
     */
    public MessageCodecHandlerBase(String channelID, IMessageCodecListener<T> messageCodecListener) {
        this.messageCodecListener = messageCodecListener;
        this.channelID = channelID;

    }

    /**
     * Gets channel id.
     *
     * @return the channel id
     */
    public String getChannelID() {
        return channelID;
    }

    /**
     * Decode message t.
     *
     * @param channelHandlerContext the channel handler context
     * @param byteBuf               the byte buf
     * @return the t
     */
    protected abstract T decodeMessage(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        MsgTracer msgTracer = new MsgTracer();
        msgTracer.put("ChannelId", channelID);
//        msgTracer.put("SessionId", channelHandlerContext.channel().id().toString());
//        msgTracer.put("SessionInfo", channelHandlerContext.channel().toString());
        T isoMsg = null;
        Throwable exception = null;
        try {
            if (messageCodecListener != null) {
                messageCodecListener.beforeDecode(channelHandlerContext);
            }
            isoMsg = decodeMessage(channelHandlerContext, byteBuf);
            if (isoMsg != null)
                channelHandlerContext.fireChannelRead(isoMsg);
        } catch (Exception ex) {
            if (logger.isErrorEnabled())
                logger.error("decode exception", ex);
            exception = ex;

        } finally {
            try {
                if (messageCodecListener != null) {
                    Map<String, Object> additionalInfo = new HashMap<>();
                    additionalInfo.put(ContextKeyUtil.MSG_TRACE, msgTracer);
                    messageCodecListener.afterDecode(channelHandlerContext, channelID, isoMsg, exception, additionalInfo);
                }
            } finally {
                MsgTracer.eject(msgTracer);
            }


        }
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, T msg, ByteBuf byteBuf) throws Exception {
        Exception exception = null;
        T appllyMessage = msg;
        MsgTracer msgTracer = null;
        if (msg instanceof IMsgContextAware) {
            MsgContext msgContext = ((IMsgContextAware) msg).getMsgContext();
            if (msgContext != null)
                msgTracer = msgContext.getTracer();
            if (msgTracer != null)
                msgTracer.inject();
        }
        try {
            if (messageCodecListener != null)
                messageCodecListener.beforeEncode(channelHandlerContext, appllyMessage);
            encodeMessage(channelHandlerContext, appllyMessage, byteBuf);
        } catch (Exception ex) {
            if (logger.isErrorEnabled())
                logger.error("encode exception", ex);
            exception = ex;
        } finally {
            try {
                if (messageCodecListener != null)
                    messageCodecListener.afterEncode(channelHandlerContext, appllyMessage, exception);
            } finally {
                if (msgTracer != null)
                    MsgTracer.eject(msgTracer);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (logger.isErrorEnabled())
            logger.error("exceptionCaught in decode.channel={} close context", ctx.channel(), cause);
        super.exceptionCaught(ctx, cause);
    }

    /**
     * Encode message.
     *
     * @param channelHandlerContext the channel handler context
     * @param m                     the m
     * @param byteBuf               the byte buf
     * @throws Exception the exception
     */
    protected abstract void encodeMessage(ChannelHandlerContext channelHandlerContext, T m, ByteBuf byteBuf) throws Exception;

}