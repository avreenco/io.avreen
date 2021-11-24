package io.avreen.iso8583.channel.tcp;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.codec.tcp.IMessageHeaderCodec;
import io.avreen.common.codec.tcp.IMessageLenCodec;
import io.avreen.common.context.MessageTypes;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.SystemPropUtil;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.packager.api.IRejectMsgPackager;
import io.avreen.iso8583.packager.api.ISOMsgPackager;
import io.avreen.iso8583.util.ISOUtil;
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
    private ISOMsgPackager isoPackager;
    private static boolean debugBodyBuffer = SystemPropUtil.getBoolean("io.avreen.encoder.iso.debug.buffer.body", false);

    /**
     * Instantiates a new Iso msg encoder.
     *
     * @param messageLenCodec    the message len codec
     * @param messageHeaderCodec the message header codec
     * @param isoPackager        the iso packager
     */
    public ISOMsgEncoder(IMessageLenCodec messageLenCodec, IMessageHeaderCodec messageHeaderCodec, ISOMsgPackager isoPackager) {
        this.isoPackager = isoPackager;
        this.messageLenCodec = messageLenCodec;
        this.messageHeaderCodec = messageHeaderCodec;

    }

    /**
     * Instantiates a new Iso msg encoder.
     *
     * @param messageLenCodec the message len codec
     * @param isoPackager     the iso packager
     */
    public ISOMsgEncoder(IMessageLenCodec messageLenCodec, ISOMsgPackager isoPackager) {
        this.isoPackager = isoPackager;
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

        if (m.getMsgContext() != null)
            MsgTracer.inject(m.getMsgContext().getTracer());

        if (logger.isDebugEnabled())
            logger.debug("encode channel={}", channelHandlerContext.channel());

        if (logger.isDebugEnabled())
            logger.debug("encode message={}", m);


        int headerLen = 0;
        if (messageHeaderCodec != null)
            headerLen = messageHeaderCodec.getHeaderLength();


        ByteBuffer byteBuffer;
        if (allocateDirect)
            byteBuffer = ByteBuffer.allocateDirect(maxMessageSize);
        else
            byteBuffer = ByteBuffer.allocate(maxMessageSize);

        byteBuffer.position(messageLenCodec.getLengthBytes() + headerLen);

        int pkgLen = 0;
        int currentPosition = byteBuffer.position();
        if (m.isReject() && m.getRejectBuffer() != null) {
            if (isoPackager instanceof IRejectMsgPackager) {
                pkgLen = ((IRejectMsgPackager) isoPackager).pack(m.getRejectBuffer(), byteBuffer);
            } else {
                pkgLen = m.getRejectBuffer().length;
                byteBuffer.put(m.getRejectBuffer());
            }
        } else {
            pkgLen = isoPackager.pack(m, byteBuffer);
            if (logger.isDebugEnabled())
                logger.debug("body total bytes={}", pkgLen);
            if (logger.isDebugEnabled()) {
                if (pkgLen > 0) {
                    byte[] b = new byte[pkgLen];
                    byteBuffer.position(currentPosition);
                    byteBuffer.get(b);
                    if (debugBodyBuffer && logger.isDebugEnabled())
                        logger.debug("encode body buffer={}", ISOUtil.hexString(b));

                }
            }

        }

        byteBuffer.flip();
        int totalLen = pkgLen + headerLen;
        if (logger.isDebugEnabled())
            logger.debug("total len encode={}", totalLen);
        byte[] lengthBytes = messageLenCodec.encodeMessageLength(pkgLen + headerLen);
        if (logger.isDebugEnabled())
            logger.debug("len encode bytes={}", ISOUtil.hexString(lengthBytes));

        byteBuffer.put(lengthBytes);

        if (headerLen > 0) {
            MessageTypes mt = MessageTypes.Response;
            if (m.isReject())
                mt = MessageTypes.Reject;
            else if (m.isRequest())
                mt = MessageTypes.Request;
            byte[] isoHeader = messageHeaderCodec.encodeHeader(m.getISOHeader(), mt, m.getRejectCode());
            if (logger.isDebugEnabled())
                logger.debug("header encode bytes={}", ISOUtil.hexString(isoHeader));
            byteBuffer.put(isoHeader);
        }

        byteBuffer.rewind();
        byteBuf.writeBytes(byteBuffer);
        byteBuffer.clear();

    }
}