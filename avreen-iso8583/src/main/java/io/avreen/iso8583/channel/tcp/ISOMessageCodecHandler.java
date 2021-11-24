package io.avreen.iso8583.channel.tcp;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.log.LoggerDomain;
import io.avreen.common.netty.IMessageCodecListener;
import io.avreen.common.netty.MessageCodecHandlerBase;
import io.avreen.iso8583.common.ISOMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.Arrays;


/**
 * The class Iso message codec handler.
 */
public class ISOMessageCodecHandler extends MessageCodecHandlerBase<ISOMsg> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".iso8583.channel.tcp.ISOMessageCodecHandler");
    private ISOTcpChannelProperties isoTcpChannelProperties;

    /**
     * Instantiates a new Iso message codec handler.
     *
     * @param isoTcpChannelProperties the iso tcp channel properties
     * @param channelID               the channel id
     * @param decodeListener          the decode listener
     */
    public ISOMessageCodecHandler(ISOTcpChannelProperties isoTcpChannelProperties, String channelID, IMessageCodecListener<ISOMsg> decodeListener) {
        super(channelID, decodeListener);
        this.isoTcpChannelProperties = isoTcpChannelProperties;
    }

    protected ISOMsg decodeMessage(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {

        ISOMsgDecoder isoMessageDecoder = new ISOMsgDecoder(isoTcpChannelProperties.getMessageLenCodec(), isoTcpChannelProperties.getMessageHeaderCodec(), isoTcpChannelProperties.getIsoPackager());
        ISOMsgDecoder.DecodeResult decodeResult = isoMessageDecoder.decode(channelHandlerContext, byteBuf, isoTcpChannelProperties.isNeedRawBuffer(), isoTcpChannelProperties.isAllocateDirect());
        return decodeResult.getIsoMsg();
    }

    @Override
    protected void encodeMessage(ChannelHandlerContext channelHandlerContext, ISOMsg msg, ByteBuf byteBuf) throws Exception {
        try {
            encodeISOMsg(channelHandlerContext, msg, byteBuf);
        } finally {
            if (msg.getRejectBuffer() != null)
                Arrays.fill(msg.getRejectBuffer(), (byte) 0);
        }
    }

    /**
     * Encode iso msg.
     *
     * @param channelHandlerContext the channel handler context
     * @param m                     the m
     * @param byteBuf               the byte buf
     * @throws Exception the exception
     */
    protected void encodeISOMsg(ChannelHandlerContext channelHandlerContext, ISOMsg m, ByteBuf byteBuf) throws Exception {

        ISOMsgEncoder messageEncoder = new ISOMsgEncoder(isoTcpChannelProperties.getMessageLenCodec(), isoTcpChannelProperties.getMessageHeaderCodec(), isoTcpChannelProperties.getIsoPackager());
        messageEncoder.encode(m, channelHandlerContext, byteBuf, isoTcpChannelProperties.getMaxMessageSize(), isoTcpChannelProperties.isAllocateDirect());
    }

}