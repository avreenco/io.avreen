package io.avreen.iso8583.channel.tcp;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.codec.tcp.IDynamicHeaderCodec;
import io.avreen.common.codec.tcp.IMessageHeaderCodec;
import io.avreen.common.codec.tcp.IMessageLenCodec;
import io.avreen.common.context.MessageTypes;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.CodecUtil;
import io.avreen.common.util.SystemPropUtil;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.mapper.api.ISOMsgMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.ByteBuffer;


/**
 * The class Iso msg encoder.
 */
class ISOMsgEncoder {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".iso8583.channel.tcp.ISOMsgEncoder");
    private IMessageLenCodec messageLenCodec;
    private IMessageHeaderCodec messageHeaderCodec;
    private ISOMsgMapper isoMsgMapper;
    private static boolean debugBodyBuffer = SystemPropUtil.getBoolean("io.avreen.encoder.iso.debug.buffer.body", false);

    /**
     * Instantiates a new Iso msg encoder.
     *
     * @param messageLenCodec    the message len codec
     * @param messageHeaderCodec the message header codec
     * @param isoMsgMapper        the iso mapper
     */
    public ISOMsgEncoder(IMessageLenCodec messageLenCodec, IMessageHeaderCodec messageHeaderCodec, ISOMsgMapper isoMsgMapper) {
        this.isoMsgMapper = isoMsgMapper;
        this.messageLenCodec = messageLenCodec;
        this.messageHeaderCodec = messageHeaderCodec;

    }

    /**
     * Instantiates a new Iso msg encoder.
     *
     * @param messageLenCodec the message len codec
     * @param isoMsgMapper     the iso mapper
     */
    public ISOMsgEncoder(IMessageLenCodec messageLenCodec, ISOMsgMapper isoMsgMapper) {
        this.isoMsgMapper = isoMsgMapper;
        this.messageLenCodec = messageLenCodec;
    }

    /**
     * Encode.
     *
     * @param m                     the m
     * @param channelHandlerContext the channel handler context
     * @param byteBuf               the byte buf
     * @param maxMessageSize        the max message size
     * @param allocateDirect        the allocate direct
     * @throws Exception the exception
     */
    protected void encode(ISOMsg m, ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, int maxMessageSize, boolean allocateDirect) throws Exception {
        if (messageLenCodec == null)
            throw new RuntimeException("message len codec is null");
        m.reCalcBitMap();
        if (m.getMsgContext() != null)
            MsgTracer.inject(m.getMsgContext().getTracer());

        if (logger.isDebugEnabled())
            logger.debug("encode channel={}", channelHandlerContext.channel());

        if (logger.isDebugEnabled())
            logger.debug("encode message={}", m);
        int headerLen = 0;
        IDynamicHeaderCodec dynamicHeaderCodec = null;
        if (messageHeaderCodec != null) {
            headerLen = m.getHeader().length;
            if (messageHeaderCodec instanceof IDynamicHeaderCodec) {
                dynamicHeaderCodec = (IDynamicHeaderCodec) messageHeaderCodec;
                headerLen += dynamicHeaderCodec.getHeaderLenCodec().getLengthBytes();
            }
        }


        ByteBuffer byteBuffer;
        if (allocateDirect)
            byteBuffer = ByteBuffer.allocateDirect(maxMessageSize);
        else
            byteBuffer = ByteBuffer.allocate(maxMessageSize);

        byteBuffer.position(messageLenCodec.getLengthBytes() + headerLen);


        int currentPosition = byteBuffer.position();
        isoMsgMapper.write(m, byteBuffer);
        int pkgLen = byteBuffer.position()-currentPosition;
        if (logger.isDebugEnabled())
            logger.debug("body total bytes={}", pkgLen);
        if (logger.isDebugEnabled()) {
            if (pkgLen > 0) {
                byte[] b = new byte[pkgLen];
                byteBuffer.position(currentPosition);
                byteBuffer.get(b);
                if (debugBodyBuffer && logger.isDebugEnabled())
                    logger.debug("encode body buffer={}", CodecUtil.hexString(b));

            }

        }
        byteBuffer.flip();
        int totalLen = pkgLen + headerLen;
        if (logger.isDebugEnabled())
            logger.debug("total len encode={}", totalLen);
        byte[] lengthBytes = messageLenCodec.encodeMessageLength(pkgLen + headerLen);
        if (logger.isDebugEnabled())
            logger.debug("len encode bytes={}", CodecUtil.hexString(lengthBytes));

        byteBuffer.put(lengthBytes);
        if (headerLen > 0) {
            MessageTypes mt = MessageTypes.Response;
            if (m.isReject())
                mt = MessageTypes.Reject;
            else if (m.isRequest())
                mt = MessageTypes.Request;
            byte[] isoHeader = messageHeaderCodec.encodeHeader(m.getHeader(), mt, m.getRejectCode());
            if (logger.isDebugEnabled())
                logger.debug("header encode bytes={}", CodecUtil.hexString(isoHeader));
            if(dynamicHeaderCodec!=null)
            {
                byte[] headerLenBytes = dynamicHeaderCodec.getHeaderLenCodec().encodeMessageLength(isoHeader.length);
                byteBuffer.put(headerLenBytes);
            }
            byteBuffer.put(isoHeader);
        }

        byteBuffer.rewind();
        byteBuf.writeBytes(byteBuffer);
        byteBuffer.clear();

    }
}