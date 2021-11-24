package io.avreen.iso8583.channel.tcp;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.codec.tcp.IMessageHeaderCodec;
import io.avreen.common.codec.tcp.IMessageLenCodec;
import io.avreen.common.codec.tcp.RejectCodeSupportCodec;
import io.avreen.common.context.ISystemRejectCodes;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.SystemPropUtil;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.packager.api.ISOMsgPackager;
import io.avreen.iso8583.util.ISOUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.nio.ByteBuffer;


/**
 * The class Iso msg decoder.
 */
class ISOMsgDecoder {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".iso8583.channel.tcp.ISOMsgDecoder");
    private IMessageLenCodec messageLenCodec;
    private IMessageHeaderCodec messageHeaderCodec;
    private ISOMsgPackager isoPackager;
    private static boolean debugBodyBuffer = SystemPropUtil.getBoolean("io.avreen.decoder.iso.debug.buffer.body", false);


    /**
     * Instantiates a new Iso msg decoder.
     *
     * @param messageLenCodec    the message len codec
     * @param messageHeaderCodec the message header codec
     * @param isoPackager        the iso packager
     */
    public ISOMsgDecoder(IMessageLenCodec messageLenCodec, IMessageHeaderCodec messageHeaderCodec, ISOMsgPackager isoPackager) {
        this.isoPackager = isoPackager;
        this.messageLenCodec = messageLenCodec;
        this.messageHeaderCodec = messageHeaderCodec;

    }

    /**
     * Instantiates a new Iso msg decoder.
     *
     * @param messageLenCodec the message len codec
     * @param isoPackager     the iso packager
     */
    public ISOMsgDecoder(IMessageLenCodec messageLenCodec, ISOMsgPackager isoPackager) {
        this.isoPackager = isoPackager;
        this.messageLenCodec = messageLenCodec;
    }

    private void setRejectBuffer(ISOMsg rejectMsg, byte[] lenBytes, byte[] header, ByteBuffer byteBuffer) {
        byte[] body = lenBytes;
        if (header != null)
            body = ISOUtil.concat(body, header);
        if (byteBuffer != null) {
            byteBuffer.rewind();
            byte[] unpackBytes = new byte[byteBuffer.limit()];
            byteBuffer.get(unpackBytes);
            body = ISOUtil.concat(body, unpackBytes);
        }
        rejectMsg.setRejectBuffer(body);

    }

    /**
     * Decode decode result.
     *
     * @param channelHandlerContext the channel handler context
     * @param byteBuf               the byte buf
     * @param needRawBuffer         the need raw buffer
     * @param allocateDirect        the allocate direct
     * @return the decode result
     */
    protected DecodeResult decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, boolean needRawBuffer, boolean allocateDirect) {

        try {
            if (messageLenCodec == null)
                throw new RuntimeException("message len codec is null");
            if (logger.isDebugEnabled())
                logger.debug("decode channel={}", channelHandlerContext.channel());
            int messageLength = messageLenCodec.getLengthBytes();
            int headerLen = 0;
            if (messageHeaderCodec != null)
                headerLen = messageHeaderCodec.getHeaderLength();
            int totalHeader = messageLength + headerLen;

            if (byteBuf.readableBytes() < totalHeader) {
                if (logger.isWarnEnabled())
                    logger.warn("read buffer pool is full temporary for read iso message length&header readableBytes={} expect={}", byteBuf.readableBytes(), totalHeader);
                return new DecodeResult(DecodeStatus.DecodeLenNotReady);
            }
            byte[] lenAndHeaderBytes = new byte[totalHeader];
            byteBuf.readBytes(lenAndHeaderBytes);
            int len = 0;
            try {
                byte[] lenBytes = new byte[messageLength];
                System.arraycopy(lenAndHeaderBytes, 0, lenBytes, 0, messageLength);
                if (logger.isDebugEnabled())
                    logger.debug("receive len bytes={}", ISOUtil.hexString(lenBytes));
                len = messageLenCodec.decodeMessageLength(lenBytes);
                if (logger.isDebugEnabled())
                    logger.debug("len={}   bytes={}", len, ISOUtil.hexString(lenBytes));
            } catch (Exception ex) {
                if (logger.isErrorEnabled())
                    logger.error("invalid message len", ex);
                ISOMsg rejectMsg = null;
                rejectMsg = isoPackager.createComponent();
                rejectMsg.setRejectCode(ISystemRejectCodes.InvalidMsgLen);
                setRejectBuffer(rejectMsg, lenAndHeaderBytes, null, null);
                return new DecodeResult(DecodeStatus.DecodeLenException, rejectMsg);
            }
            if (logger.isDebugEnabled())
                logger.debug("message length={}", len);
            if (len == 0)
                return new DecodeResult(DecodeStatus.DecodeLenNotZero);
            byte[] header = null;

            if (headerLen > 0) {
                if (logger.isDebugEnabled())
                    logger.debug("header length={}", headerLen);

                header = new byte[headerLen];
                System.arraycopy(lenAndHeaderBytes, messageLength, header, 0, headerLen);
                if (logger.isDebugEnabled())
                    logger.debug("header value={}", ISOUtil.hexString(header));

                len -= headerLen;
            }
            if (byteBuf.readableBytes() < (len)) {
                byteBuf.readerIndex(byteBuf.readerIndex() - totalHeader);
                if (logger.isWarnEnabled())
                    logger.warn("read buffer pool is full temporary for read iso message body readableBytes={} expect={}", byteBuf.readableBytes(), len);
                return new ISOMsgDecoder.DecodeResult(ISOMsgDecoder.DecodeStatus.DecodeBodyNotReady);
            }
            ByteBuffer byteBuffer;
            if (allocateDirect)
                byteBuffer = ByteBuffer.allocateDirect(len);
            else
                byteBuffer = ByteBuffer.allocate(len);
            byteBuf.readBytes(byteBuffer);

            ISOMsg isoMsg = isoPackager.createComponent();

            setRejectBuffer(isoMsg, lenAndHeaderBytes, header, byteBuffer);
            isoMsg.setISOHeader(header);
            boolean rejectMessage = false;
            if (header != null) {
                if (messageHeaderCodec instanceof RejectCodeSupportCodec) {
                    if (((RejectCodeSupportCodec) messageHeaderCodec).isReject(header)) {
                        isoMsg.setRejectCode(((RejectCodeSupportCodec) messageHeaderCodec).getRejectCode(header));
                        rejectMessage = true;
                    }
                }
            }

            if (!rejectMessage) {
                byteBuffer.position(0);
                try {

                    this.isoPackager.unpack(isoMsg, byteBuffer);
                    if (needRawBuffer) {
                        byteBuffer.position(0);
                        byte[] b = new byte[byteBuffer.limit()];
                        byteBuffer.get(b);
                        isoMsg.setRawBuffer(b);
                        if (debugBodyBuffer && logger.isDebugEnabled())
                            logger.debug("decode body buffer={}", ISOUtil.hexString(b));
                    }
                } catch (Exception e) {
                    if (logger.isErrorEnabled())
                        logger.error("unpack error len={} and exception={}", len, e);
                    isoMsg.setRejectCode(ISystemRejectCodes.InvalidIsoMsg);
                }
            }

//            byteBuffer.clear();
//            for (int i = 0; i < len; i++)
//                byteBuffer.put(i, (byte) 0);

            if (logger.isDebugEnabled())
                logger.debug("decode message={}", isoMsg);
            MsgTracer msgTracer = MsgTracer.current();
            if (msgTracer != null) {
                msgTracer.put("Stan", isoMsg.getString(11));
                msgTracer.put("TerminalId", isoMsg.getString(41));
            }
            return new DecodeResult(DecodeStatus.Complete, isoMsg);
        } finally {

        }
    }

    /**
     * The enum Decode status.
     */
    enum DecodeStatus {
        /**
         * Complete decode status.
         */
        Complete,
        /**
         * Decode len exception decode status.
         */
        DecodeLenException,
        /**
         * Decode len not ready decode status.
         */
        DecodeLenNotReady,
        /**
         * Decode len not zero decode status.
         */
        DecodeLenNotZero,
        /**
         * Decode header exception decode status.
         */
        DecodeHeaderException,
        /**
         * Decode header not ready decode status.
         */
        DecodeHeaderNotReady,
        /**
         * Decode body exception decode status.
         */
        DecodeBodyException,
        /**
         * Decode body not ready decode status.
         */
        DecodeBodyNotReady,
    }

    /**
     * The class Decode result.
     */
    class DecodeResult {
        private ISOMsg isoMsg;
        private DecodeStatus decodeStatus;

        /**
         * Instantiates a new Decode result.
         *
         * @param decodeStatus the decode status
         */
        public DecodeResult(DecodeStatus decodeStatus) {
            this.decodeStatus = decodeStatus;
        }

        /**
         * Instantiates a new Decode result.
         *
         * @param decodeStatus the decode status
         * @param isoMsg       the iso msg
         */
        public DecodeResult(DecodeStatus decodeStatus, ISOMsg isoMsg) {
            this.isoMsg = isoMsg;
            this.decodeStatus = decodeStatus;
        }

        /**
         * Gets iso msg.
         *
         * @return the iso msg
         */
        public ISOMsg getIsoMsg() {
            return isoMsg;
        }

        /**
         * Gets decode status.
         *
         * @return the decode status
         */
        public DecodeStatus getDecodeStatus() {
            return decodeStatus;
        }
    }
}