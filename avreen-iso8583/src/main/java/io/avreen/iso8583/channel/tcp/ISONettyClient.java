package io.avreen.iso8583.channel.tcp;

import io.avreen.common.netty.client.NettyClientBase;
import io.avreen.iso8583.common.ISOMsg;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 * The class Iso netty client.
 */
public class ISONettyClient extends NettyClientBase<ISOMsg> implements ISONettyClientMXBean, ITcpISOChannel {
    private ISOTcpChannelProperties isoTcpChannelProperties;

    /**
     * Builder iso netty client builder.
     *
     * @return the iso netty client builder
     */
    public static ISONettyClientBuilder Builder() {
        return new ISONettyClientBuilder();
    }

    /**
     * Builder iso netty client builder.
     *
     * @param host the host
     * @param port the port
     * @return the iso netty client builder
     */
    public static ISONettyClientBuilder Builder(String host, int port) {
        return new ISONettyClientBuilder().host(host).port(port);
    }

    /**
     * Instantiates a new Iso netty client.
     */
    protected ISONettyClient() {
    }

    /**
     * Instantiates a new Iso netty client.
     *
     * @param host                    the host
     * @param port                    the port
     * @param isoTcpChannelProperties the iso tcp channel properties
     */
    protected ISONettyClient(String host, int port, ISOTcpChannelProperties isoTcpChannelProperties) {
        super(host, port);
        this.isoTcpChannelProperties = isoTcpChannelProperties;
    }

    /**
     * Instantiates a new Iso netty client.
     *
     * @param host the host
     * @param port the port
     */
    public ISONettyClient(String host, int port) {
        super(host, port);
    }

    /**
     * Gets iso tcp channel properties.
     *
     * @return the iso tcp channel properties
     */
    public ISOTcpChannelProperties getIsoTcpChannelProperties() {
        if (isoTcpChannelProperties == null)
            isoTcpChannelProperties = new ISOTcpChannelProperties();
        return isoTcpChannelProperties;
    }

    /**
     * Sets iso tcp channel properties.
     *
     * @param isoTcpChannelProperties the iso tcp channel properties
     */
    protected void setIsoTcpChannelProperties(ISOTcpChannelProperties isoTcpChannelProperties) {
        this.isoTcpChannelProperties = isoTcpChannelProperties;
    }

    @Override
    protected void initPipeline(ChannelPipeline channelPipeline) {
        channelPipeline.addLast(buildByteToMessageCodec());

    }


    /**
     * Build byte to message codec byte to message codec.
     *
     * @return the byte to message codec
     */
    public ByteToMessageCodec buildByteToMessageCodec() {
        ISOMessageCodecHandler isoMessageDecoderHandler = new ISOMessageCodecHandler(isoTcpChannelProperties, getChannelId(), this);
        return isoMessageDecoderHandler;

    }


    @Override
    protected void initService() throws Throwable {
        super.initService();
        if (isoTcpChannelProperties == null)
            throw new RuntimeException("can not create client. isoTcpChannelProperties is null");

        if (isoTcpChannelProperties.getMessageLenCodec() == null)
            throw new RuntimeException("can not create client. lenHeaderCodec is null");
        if (isoTcpChannelProperties.getIsoPackager() == null)
            throw new RuntimeException("can not create client. iso packager is null");

    }

    /**
     * Gets codec string.
     *
     * @return the codec string
     */
    public String getCodecString() {
        if (isoTcpChannelProperties != null)
            return isoTcpChannelProperties.toString();
        return null;
    }


    @Override
    public String getChannelProps() {
        if (isoTcpChannelProperties == null)
            return "";
        return isoTcpChannelProperties.toString();
    }

    @Override
    public String getType() {
        return "iso-client";
    }

    @Override
    public ISOTcpChannelProperties getProperties() {
        return isoTcpChannelProperties;
    }
}
