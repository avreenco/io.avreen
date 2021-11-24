package io.avreen.iso8583.channel.tcp;

import io.avreen.common.codec.tcp.IMessageHeaderCodec;
import io.avreen.common.codec.tcp.IMessageLenCodec;
import io.avreen.common.netty.server.NettyServerBaseBuilder;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.packager.api.ISOMsgPackager;

/**
 * The class Iso netty server builder.
 */
public class ISONettyServerBuilder extends NettyServerBaseBuilder<ISONettyServer, ISOMsg, ISONettyServerBuilder> {
    /**
     * Instantiates a new Iso netty server builder.
     */
    protected ISONettyServerBuilder() {
        super(new ISONettyServer());
    }

    /**
     * Channel properties iso netty server builder.
     *
     * @param channelProperties the channel properties
     * @return the iso netty server builder
     */
    public ISONettyServerBuilder channelProperties(ISOTcpChannelProperties channelProperties) {
        this.channelProperties = channelProperties;
        return this;
    }

    private ISOTcpChannelProperties channelProperties = new ISOTcpChannelProperties();


    /**
     * Message len codec iso netty server builder.
     *
     * @param messageLenCodec the message len codec
     * @return the iso netty server builder
     */
    public ISONettyServerBuilder messageLenCodec(IMessageLenCodec messageLenCodec) {
        this.channelProperties.setMessageLenCodec(messageLenCodec);
        return this;
    }

    /**
     * Message header codec iso netty server builder.
     *
     * @param messageHeaderCodec the message header codec
     * @return the iso netty server builder
     */
    public ISONettyServerBuilder messageHeaderCodec(IMessageHeaderCodec messageHeaderCodec) {
        this.channelProperties.setMessageHeaderCodec(messageHeaderCodec);
        return this;
    }

    /**
     * Iso packager iso netty server builder.
     *
     * @param isoPackager the iso packager
     * @return the iso netty server builder
     */
    public ISONettyServerBuilder isoPackager(ISOMsgPackager isoPackager) {
        this.channelProperties.setIsoPackager(isoPackager);
        return this;
    }

    /**
     * Max message size iso netty server builder.
     *
     * @param maxMessageSize the max message size
     * @return the iso netty server builder
     */
    public ISONettyServerBuilder maxMessageSize(int maxMessageSize) {
        this.channelProperties.setMaxMessageSize(maxMessageSize);
        return this;
    }

    /**
     * Allocate direct iso netty server builder.
     *
     * @param allocateDirect the allocate direct
     * @return the iso netty server builder
     */
    public ISONettyServerBuilder allocateDirect(boolean allocateDirect) {
        channelProperties.setAllocateDirect(allocateDirect);
        return this;
    }

    /**
     * Need raw buffer iso netty server builder.
     *
     * @param needRawBuffer the need raw buffer
     * @return the iso netty server builder
     */
    public ISONettyServerBuilder needRawBuffer(boolean needRawBuffer) {
        channelProperties.setNeedRawBuffer(needRawBuffer);
        return this;
    }

    public ISONettyServer build() {
        super.build();
        if (nettyServer.getIsoTcpChannelProperties() == null)
            nettyServer.setIsoTcpChannelProperties(new ISOTcpChannelProperties());
        ISOTcpChannelProperties.applySystemDefault(channelProperties);
        nettyServer.setIsoTcpChannelProperties(channelProperties);
        return nettyServer;
    }
}

