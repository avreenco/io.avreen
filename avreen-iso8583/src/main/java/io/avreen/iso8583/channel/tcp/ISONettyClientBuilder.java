package io.avreen.iso8583.channel.tcp;

import io.avreen.common.codec.tcp.IMessageHeaderCodec;
import io.avreen.common.codec.tcp.IMessageLenCodec;
import io.avreen.common.netty.client.NettyClientBaseBuilder;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.packager.api.ISOMsgPackager;

/**
 * The class Iso netty client builder.
 */
public class ISONettyClientBuilder extends NettyClientBaseBuilder<ISONettyClient, ISOMsg, ISONettyClientBuilder> {
    /**
     * Instantiates a new Iso netty client builder.
     */
    protected ISONettyClientBuilder() {
        super(new ISONettyClient());
    }

    /**
     * Channel properties iso netty client builder.
     *
     * @param channelProperties the channel properties
     * @return the iso netty client builder
     */
    public ISONettyClientBuilder channelProperties(ISOTcpChannelProperties channelProperties) {
        this.channelProperties = channelProperties;
        return this;
    }

    private ISOTcpChannelProperties channelProperties = new ISOTcpChannelProperties();


    /**
     * Message len codec iso netty client builder.
     *
     * @param messageLenCodec the message len codec
     * @return the iso netty client builder
     */
    public ISONettyClientBuilder messageLenCodec(IMessageLenCodec messageLenCodec) {
        this.channelProperties.setMessageLenCodec(messageLenCodec);
        return this;
    }

    /**
     * Message header codec iso netty client builder.
     *
     * @param messageHeaderCodec the message header codec
     * @return the iso netty client builder
     */
    public ISONettyClientBuilder messageHeaderCodec(IMessageHeaderCodec messageHeaderCodec) {
        this.channelProperties.setMessageHeaderCodec(messageHeaderCodec);
        return this;
    }

    /**
     * Iso packager iso netty client builder.
     *
     * @param isoPackager the iso packager
     * @return the iso netty client builder
     */
    public ISONettyClientBuilder isoPackager(ISOMsgPackager isoPackager) {
        this.channelProperties.setIsoPackager(isoPackager);
        return this;
    }

    /**
     * Max message size iso netty client builder.
     *
     * @param maxMessageSize the max message size
     * @return the iso netty client builder
     */
    public ISONettyClientBuilder maxMessageSize(int maxMessageSize) {
        this.channelProperties.setMaxMessageSize(maxMessageSize);
        return this;
    }

    /**
     * Allocate direct iso netty client builder.
     *
     * @param allocateDirect the allocate direct
     * @return the iso netty client builder
     */
    public ISONettyClientBuilder allocateDirect(boolean allocateDirect) {
        channelProperties.setAllocateDirect(allocateDirect);
        return this;
    }

    /**
     * Need raw buffer iso netty client builder.
     *
     * @param needRawBuffer the need raw buffer
     * @return the iso netty client builder
     */
    public ISONettyClientBuilder needRawBuffer(boolean needRawBuffer) {
        channelProperties.setNeedRawBuffer(needRawBuffer);
        return this;
    }

    public ISONettyClient build() {
        super.build();
        if (delegate.getIsoTcpChannelProperties() == null)
            delegate.setIsoTcpChannelProperties(new ISOTcpChannelProperties());
        ISOTcpChannelProperties.applySystemDefault(channelProperties);
        delegate.setIsoTcpChannelProperties(channelProperties);
        return delegate;
    }
}

