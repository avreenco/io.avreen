package org.avreen.security.module.impl.hsm.safenet.channel;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.log.LoggerDomain;
import io.avreen.common.netty.IMessageCodecListener;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.avreen.security.module.api.SecurityFunctionMessage;
import org.avreen.security.module.api.SecurityUtil;
import org.avreen.security.module.impl.hsm.safenet.engine.SafeNetMsgPackager;

import java.nio.ByteBuffer;
import java.util.List;


/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Safe net msg codec handler.
 */
public class SafeNetMsgCodecHandler extends ByteToMessageCodec<SecurityFunctionMessage> {
    private static InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".security.module.impl.hsm.safenet.channel.SafeNetMsgCodecHandler");

    private IMessageCodecListener<SecurityFunctionMessage> decodeListener;
    private boolean clientMode = true;
    private int maxMessageSize = 9999;
    private boolean allocateDirect = false;

    /**
     * Instantiates a new Safe net msg codec handler.
     *
     * @param decodeListener the decode listener
     */
    public SafeNetMsgCodecHandler(IMessageCodecListener<SecurityFunctionMessage> decodeListener) {
        this.decodeListener = decodeListener;

    }

    /**
     * Is client mode boolean.
     *
     * @return the boolean
     */
    public boolean isClientMode() {
        return clientMode;
    }

    /**
     * Sets client mode.
     *
     * @param clientMode the client mode
     */
    public void setClientMode(boolean clientMode) {
        this.clientMode = clientMode;
    }

    private SecurityFunctionMessage receiveBytes(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {


        try {
            int messageWithHeaderLen = 6;
            byteBuf.markReaderIndex();
            if (byteBuf.readableBytes() < messageWithHeaderLen) {
                if (logger.isWarnEnabled())
                    logger.warn("read buffer pool is full temporary for read iso hsm message length");
                byteBuf.resetReaderIndex();
                return null;
            }
            byte[] messageWithHeader = new byte[messageWithHeaderLen];
            byteBuf.markReaderIndex();
            byteBuf.readBytes(messageWithHeader);
            if (logger.isDebugEnabled()) {
                logger.debug("decode lentgh={}", SecurityUtil.hexString(messageWithHeader));
            }

            byte[] header = SafeNetMsgPackager.decodeHeader(messageWithHeader);
            if (logger.isDebugEnabled()) {
                logger.debug("decode header={}", SecurityUtil.hexString(header));
            }

            int len = SafeNetMsgPackager.decodeMsgLength(messageWithHeader);
            if (logger.isDebugEnabled()) {
                logger.debug("length={}", len);
            }
            ByteBuffer bodyByteBuffer;
            if (allocateDirect)
                bodyByteBuffer = ByteBuffer.allocateDirect(len);
            else
                bodyByteBuffer = ByteBuffer.allocate(len);

            if (len == 0)
                return null;
            if (byteBuf.readableBytes() < len) {
                if (logger.isWarnEnabled())
                    logger.warn("read buffer pool is full temporary for read iso hsm message body");
                byteBuf.resetReaderIndex();
                return null;
            }
            byteBuf.markReaderIndex();
            byteBuf.readBytes(bodyByteBuffer);
            bodyByteBuffer.position(0);

            byte[] fc = SafeNetMsgPackager.decodeFCFromBody(bodyByteBuffer);
            if (logger.isDebugEnabled()) {
                logger.debug("decode body ={}", SecurityUtil.hexString(bodyByteBuffer));
                logger.debug("decode function code={}", SecurityUtil.hexString(fc));
            }

            bodyByteBuffer.position(0);
            SecurityFunctionMessage securityFunctionMessage;
            if (clientMode) {
                securityFunctionMessage = SecurityFunctionMessageRepository.buildResponseMsg(SecurityUtil.hexString(fc));
            } else {
                securityFunctionMessage = SecurityFunctionMessageRepository.buildRequestMessage(SecurityUtil.hexString(fc));
            }
            securityFunctionMessage.setHeader(header);
            int tLen = SafeNetMsgPackager.decodeBody(bodyByteBuffer, securityFunctionMessage);
            if (logger.isDebugEnabled()) {
                logger.debug("total decode bytes={}", tLen);
            }
            decodeListener.afterDecode(channelHandlerContext, "SafenetChannel", securityFunctionMessage, null, null);
            return securityFunctionMessage;
        } finally {
        }
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, SecurityFunctionMessage securityFunctionMessage, ByteBuf byteBuf) throws Exception {
        try {
            ByteBuffer byteBuffer = null;
            if (allocateDirect)
                byteBuffer = ByteBuffer.allocateDirect(maxMessageSize);
            else
                byteBuffer = ByteBuffer.allocate(maxMessageSize);
            int encodeLen = SafeNetMsgPackager.encode(byteBuffer, securityFunctionMessage);
            if (logger.isDebugEnabled()) {
                logger.debug("total encode bytes={}", encodeLen);
                logger.debug("encode bytes={}", SecurityUtil.hexString(byteBuffer));
            }
            byteBuffer.limit(encodeLen);
            byteBuffer.rewind();
            byteBuf.writeBytes(byteBuffer);
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error(e.getMessage(), e);
        }

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        receiveBytes(channelHandlerContext, byteBuf);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (logger.isErrorEnabled())
            logger.error("exceptionCaught in decode. close context", cause);
        ctx.close();
    }
}