package io.avreen.hb.channel.tcp;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.codec.tcp.IMessageHeaderCodec;
import io.avreen.common.codec.tcp.IMessageLenCodec;
import io.avreen.common.context.MessageTypes;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.netty.IMessageCodecListener;
import io.avreen.common.netty.MessageCodecHandlerBase;
import io.avreen.hb.HeaderBodyObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.ByteBuffer;


/**
 * The class Header body message codec handler.
 */
public class HeaderBodyMessageCodecHandler extends MessageCodecHandlerBase<HeaderBodyObject> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".hb.channel.tcp.HeaderBodyMessageCodecHandler");
    private IMessageLenCodec messageLenCodec;
    private IMessageHeaderCodec messageHeaderCodec;
    private boolean allocateDirect = false;
    private MessageTypes messageType;
    private int maxMessageSize = 9999;

    /**
     * Instantiates a new Header body message codec handler.
     *
     * @param channelID     the channel id
     * @param codecListener the codec listener
     * @param messageType   the message type
     */
    public HeaderBodyMessageCodecHandler(String channelID, IMessageCodecListener<HeaderBodyObject> codecListener, MessageTypes messageType) {
        super(channelID, codecListener);
        this.messageType = messageType;

    }

    /**
     * Gets max message size.
     *
     * @return the max message size
     */
    public int getMaxMessageSize() {
        return maxMessageSize;
    }

    /**
     * Sets max message size.
     *
     * @param maxMessageSize the max message size
     */
    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    /**
     * Sets message len codec.
     *
     * @param messageLenCodec the message len codec
     */
    public void setMessageLenCodec(IMessageLenCodec messageLenCodec) {
        this.messageLenCodec = messageLenCodec;
    }

    /**
     * Sets message header codec.
     *
     * @param messageHeaderCodec the message header codec
     */
    public void setMessageHeaderCodec(IMessageHeaderCodec messageHeaderCodec) {
        this.messageHeaderCodec = messageHeaderCodec;
    }

    /**
     * Is allocate direct boolean.
     *
     * @return the boolean
     */
    public boolean isAllocateDirect() {
        return allocateDirect;
    }

    /**
     * Sets allocate direct.
     *
     * @param allocateDirect the allocate direct
     */
    public void setAllocateDirect(boolean allocateDirect) {
        this.allocateDirect = allocateDirect;
    }


    public HeaderBodyObject decodeMessage(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {

        try {
            if (messageLenCodec == null)
                throw new RuntimeException("MessageLenCodec property is null");

            int messageLength = messageLenCodec.getLengthBytes();
            byteBuf.markReaderIndex();
            if (byteBuf.readableBytes() < messageLength) {
                if (logger.isWarnEnabled())
                    logger.warn("read buffer pool is full temporary for read iso message length readableBytes={} expect={}", byteBuf.readableBytes(), messageLength);
                byteBuf.resetReaderIndex();
                return null;
            }
            byte[] lenBytes = new byte[messageLength];
            byteBuf.markReaderIndex();
            byteBuf.readBytes(lenBytes);

            int len = 0;
            try {
                len = messageLenCodec.decodeMessageLength(lenBytes);
            } catch (Exception ex) {
                if (logger.isErrorEnabled())
                    logger.error("invalid message len", ex);
                return null;
            }
            if (logger.isDebugEnabled())
                logger.debug("message length={}", len);
            if (len == 0)
                return null;
            int hLen = 0;
            if (messageHeaderCodec != null)
                hLen = messageHeaderCodec.getHeaderLength();

            byte[] header = null;

            if (hLen > 0) {
                header = new byte[hLen];
                if (byteBuf.readableBytes() < hLen) {
                    if (logger.isWarnEnabled())
                        logger.warn("read buffer pool is full temporary for read iso message isoHeader readableBytes={} expect={}", byteBuf.readableBytes(), hLen);
                    byteBuf.resetReaderIndex();
                    return null;
                }
                byteBuf.markReaderIndex();
                byteBuf.readBytes(header);
                len -= header.length;
            }
            if (byteBuf.readableBytes() < len) {
                if (logger.isWarnEnabled())
                    logger.warn("read buffer pool is full temporary for read iso message body readableBytes={} expect={}", byteBuf.readableBytes(), len);
                byteBuf.resetReaderIndex();
                return null;
            }
            byteBuf.markReaderIndex();
            byte[] bodyBytes = new byte[len];

            byteBuf.readBytes(bodyBytes);

            HeaderBodyObject headerBodyObject = new HeaderBodyObject();
            headerBodyObject.setHeader(header);
            headerBodyObject.setBody(bodyBytes);

            return headerBodyObject;
        } finally {
        }
    }

    @Override
    protected void encodeMessage(ChannelHandlerContext channelHandlerContext, HeaderBodyObject m, ByteBuf byteBuf) {
        if (logger.isDebugEnabled())
            logger.debug("encoding message={}", m);
        if (messageLenCodec == null)
            throw new RuntimeException("MessageLenCodec property is null");

        int headerLen = 0;
        if (messageHeaderCodec != null)
            headerLen = messageHeaderCodec.getHeaderLength();
        ByteBuffer byteBuffer;
        if (isAllocateDirect())
            byteBuffer = ByteBuffer.allocateDirect(maxMessageSize);
        else
            byteBuffer = ByteBuffer.allocate(maxMessageSize);

        byteBuffer.position(messageLenCodec.getLengthBytes() + headerLen);
        int pkgLen = 0;
        MessageTypes msgType = this.messageType;
        if (m.isReject())
            msgType = MessageTypes.Reject;
        pkgLen = m.getBody().length;
        byteBuffer.put(m.getBody());
        byteBuffer.flip();
        byte[] lengthBytes = messageLenCodec.encodeMessageLength(pkgLen + headerLen);

        byteBuffer.put(lengthBytes);

        if (headerLen > 0) {
            byte[] header = messageHeaderCodec.encodeHeader(m.getHeader(), msgType, m.getRejectCode());
            byteBuffer.put(header);
        }

        byteBuffer.rewind();
        byteBuf.writeBytes(byteBuffer);
        byteBuffer.clear();

    }

}