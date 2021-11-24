package io.avreen.iso8583.channel.tcp;

import io.avreen.common.codec.tcp.ASCIIMessageLenCodec;
import io.avreen.common.codec.tcp.IMessageHeaderCodec;
import io.avreen.common.codec.tcp.IMessageLenCodec;
import io.avreen.common.util.SimpleToStringUtil;
import io.avreen.iso8583.packager.api.ISOMsgPackager;
import io.avreen.iso8583.packager.impl.ISO87APackager;

/**
 * The class Iso tcp channel properties.
 */
public class ISOTcpChannelProperties {
    private IMessageLenCodec messageLenCodec;
    private IMessageHeaderCodec messageHeaderCodec;
    private ISOMsgPackager isoPackager;
    private int maxMessageSize = 9999;
    private boolean allocateDirect = false;
    private boolean needRawBuffer = false;

    /**
     * Instantiates a new Iso tcp channel properties.
     */
    public ISOTcpChannelProperties() {

    }

    /**
     * Instantiates a new Iso tcp channel properties.
     *
     * @param messageLenCodec the message len codec
     * @param isoPackager     the iso packager
     */
    public ISOTcpChannelProperties(IMessageLenCodec messageLenCodec, ISOMsgPackager isoPackager) {
        this.messageLenCodec = messageLenCodec;
        this.isoPackager = isoPackager;
    }

    /**
     * Instantiates a new Iso tcp channel properties.
     *
     * @param messageLenCodec    the message len codec
     * @param messageHeaderCodec the message header codec
     * @param isoPackager        the iso packager
     */
    public ISOTcpChannelProperties(IMessageLenCodec messageLenCodec, IMessageHeaderCodec messageHeaderCodec, ISOMsgPackager isoPackager) {
        this.messageLenCodec = messageLenCodec;
        this.messageHeaderCodec = messageHeaderCodec;
        this.isoPackager = isoPackager;
    }

    @Override
    public String toString() {
        return SimpleToStringUtil.toString(this);
    }

    /**
     * Gets message len codec.
     *
     * @return the message len codec
     */
    public IMessageLenCodec getMessageLenCodec() {
        return messageLenCodec;
    }

    /**
     * Sets message len codec.
     *
     * @param messageLenCodec the message len codec
     * @return the message len codec
     */
    public ISOTcpChannelProperties setMessageLenCodec(IMessageLenCodec messageLenCodec) {
        this.messageLenCodec = messageLenCodec;
        return this;
    }

    /**
     * Gets message header codec.
     *
     * @return the message header codec
     */
    public IMessageHeaderCodec getMessageHeaderCodec() {
        return messageHeaderCodec;
    }

    /**
     * Sets message header codec.
     *
     * @param messageHeaderCodec the message header codec
     * @return the message header codec
     */
    public ISOTcpChannelProperties setMessageHeaderCodec(IMessageHeaderCodec messageHeaderCodec) {
        this.messageHeaderCodec = messageHeaderCodec;
        return this;
    }

    /**
     * Gets iso packager.
     *
     * @return the iso packager
     */
    public ISOMsgPackager getIsoPackager() {
        return isoPackager;
    }

    /**
     * Sets iso packager.
     *
     * @param isoPackager the iso packager
     * @return the iso packager
     */
    public ISOTcpChannelProperties setIsoPackager(ISOMsgPackager isoPackager) {
        this.isoPackager = isoPackager;
        return this;
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
     * @return the max message size
     */
    public ISOTcpChannelProperties setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
        return this;
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
     * @return the allocate direct
     */
    public ISOTcpChannelProperties setAllocateDirect(boolean allocateDirect) {
        this.allocateDirect = allocateDirect;
        return this;
    }

    /**
     * Is need raw buffer boolean.
     *
     * @return the boolean
     */
    public boolean isNeedRawBuffer() {
        return needRawBuffer;
    }

    /**
     * Sets need raw buffer.
     *
     * @param needRawBuffer the need raw buffer
     * @return the need raw buffer
     */
    public ISOTcpChannelProperties setNeedRawBuffer(boolean needRawBuffer) {
        this.needRawBuffer = needRawBuffer;
        return this;
    }

    /**
     * Apply system default.
     *
     * @param channelProperties the channel properties
     */
    public static void applySystemDefault(ISOTcpChannelProperties channelProperties) {
        if (channelProperties == null)
            return;
        if (channelProperties.getMessageLenCodec() == null)
            channelProperties.setMessageLenCodec(new ASCIIMessageLenCodec(4, 9999));
        if (channelProperties.getIsoPackager() == null)
            channelProperties.setIsoPackager(new ISO87APackager());
    }

}
