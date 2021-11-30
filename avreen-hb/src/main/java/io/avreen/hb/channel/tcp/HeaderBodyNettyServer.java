package io.avreen.hb.channel.tcp;

import io.avreen.common.codec.tcp.IMessageHeaderCodec;
import io.avreen.common.codec.tcp.IMessageLenCodec;
import io.avreen.common.context.MessageTypes;
import io.avreen.common.netty.server.NettyServerBase;
import io.avreen.hb.HeaderBodyObject;
import io.netty.channel.ChannelPipeline;

/**
 * The class Header body netty server.
 */
public class HeaderBodyNettyServer extends NettyServerBase<HeaderBodyObject> {
    private IMessageLenCodec messageLenCodec;
    private IMessageHeaderCodec messageHeaderCodec;
    private int maxMessageSize = 9999;
    private boolean allocateDirect = false;
    private String channelID;

    /**
     * Instantiates a new Header body netty server.
     */
    public HeaderBodyNettyServer() {

    }

    /**
     * Instantiates a new Header body netty server.
     *
     * @param port            the port
     * @param messageLenCodec the message len codec
     */
    public HeaderBodyNettyServer(int port, IMessageLenCodec messageLenCodec) {
        this(port, messageLenCodec, null);


    }

    /**
     * Instantiates a new Header body netty server.
     *
     * @param port               the port
     * @param messageLenCodec    the message len codec
     * @param messageHeaderCodec the message header codec
     */
    public HeaderBodyNettyServer(int port, IMessageLenCodec messageLenCodec, IMessageHeaderCodec messageHeaderCodec) {
        super(port);
        this.messageLenCodec = messageLenCodec;
        this.messageHeaderCodec = messageHeaderCodec;
    }

    /**
     * Builder header body netty server.
     *
     * @return the header body netty server
     */
    public static HeaderBodyNettyServer Builder() {
        return new HeaderBodyNettyServer();
    }

    @Override
    protected void initPipeline(ChannelPipeline channelPipeline) {
        channelPipeline.addLast(buildByteToMessageCodec());

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
     */
    public void setMessageLenCodec(IMessageLenCodec messageLenCodec) {
        this.messageLenCodec = messageLenCodec;
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
     */
    public void setMessageHeaderCodec(IMessageHeaderCodec messageHeaderCodec) {
        this.messageHeaderCodec = messageHeaderCodec;
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

    @Override
    protected void initService() throws Throwable {
        super.initService();
        if (messageLenCodec == null)
            throw new RuntimeException("can not create message encoder. messageLenCodec is null");

    }


    /**
     * Build byte to message codec header body message codec handler.
     *
     * @return the header body message codec handler
     */
    public HeaderBodyMessageCodecHandler buildByteToMessageCodec() {
        HeaderBodyMessageCodecHandler messageCodecHandler = new HeaderBodyMessageCodecHandler(channelID == null ? getName() : channelID, this, MessageTypes.Response);
        messageCodecHandler.setMessageLenCodec(messageLenCodec);
        messageCodecHandler.setMessageHeaderCodec(messageHeaderCodec);
        messageCodecHandler.setAllocateDirect(allocateDirect);
        messageCodecHandler.setAllocateDirect(allocateDirect);
        messageCodecHandler.setMaxMessageSize(maxMessageSize);
        return messageCodecHandler;
    }

    /**
     * Sets channel id.
     *
     * @param channelID the channel id
     */
    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }


    @Override
    public String getType() {
        return "hb-server";
    }
}

