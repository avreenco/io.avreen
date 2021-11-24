
package io.avreen.iso8583.channel.tcp;

import io.avreen.common.netty.server.NettyServerBase;
import io.avreen.iso8583.common.ISOMsg;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 * The class Iso netty server.
 */
public class ISONettyServer extends NettyServerBase<ISOMsg> implements ISONettyServerMXBean, ITcpISOChannel {

    private ISOTcpChannelProperties isoTcpChannelProperties;

    /**
     * Builder iso netty server builder.
     *
     * @return the iso netty server builder
     */
    public static ISONettyServerBuilder Builder() {
        return new ISONettyServerBuilder();
    }

    /**
     * Builder iso netty server builder.
     *
     * @param port the port
     * @return the iso netty server builder
     */
    public static ISONettyServerBuilder Builder(int port) {
        return new ISONettyServerBuilder().port(port);
    }


    /**
     * Instantiates a new Iso netty server.
     */
    protected ISONettyServer() {

    }

    /**
     * Instantiates a new Iso netty server.
     *
     * @param port the port
     */
    protected ISONettyServer(int port) {
        super(port);
    }

    /**
     * Instantiates a new Iso netty server.
     *
     * @param port                    the port
     * @param isoTcpChannelProperties the iso tcp channel properties
     */
    public ISONettyServer(int port, ISOTcpChannelProperties isoTcpChannelProperties) {
        super(port);
        this.isoTcpChannelProperties = isoTcpChannelProperties;
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

    @Override
    protected void initService() throws Throwable {
        super.initService();
        if (isoTcpChannelProperties == null)
            throw new RuntimeException("can not create server. isoTcpChannelProperties is null");

        if (isoTcpChannelProperties.getMessageLenCodec() == null)
            throw new RuntimeException("can not create server. lenHeaderCodec is null");
        if (isoTcpChannelProperties.getIsoPackager() == null)
            throw new RuntimeException("can not create server. iso packager is null");
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
        return "iso-server";
    }

    @Override
    public ISOTcpChannelProperties getProperties() {
        return isoTcpChannelProperties;
    }
}

