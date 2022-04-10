package io.avreen.http.channel;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.log.LoggerDomain;
import io.avreen.common.netty.IMessageCodecListener;
import io.avreen.http.common.HttpMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;


/**
 * The type Http message codec.
 */
public abstract class HttpMessageCodec extends MessageToMessageCodec<Object, Object> {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.channel.HttpMessageCodec");
    /**
     * The Codec listener.
     */
    protected IMessageCodecListener<HttpMsg> codecListener;
    /**
     * The Channel id.
     */
    protected String channelId;

    /**
     * Instantiates a new Http message codec.
     *
     * @param channelId     the channel id
     * @param codecListener the codec listener
     */
    public HttpMessageCodec(String channelId, IMessageCodecListener<HttpMsg> codecListener) {
        // this.messageClass = messageClass;
        this.codecListener = codecListener;
        this.channelId = channelId;
    }


    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (logger.isErrorEnabled())
            logger.error("exceptionCaught in decode.channel={} close context", ctx.channel(), cause);
        super.exceptionCaught(ctx, cause);
    }


}

