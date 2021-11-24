package io.avreen.hb.channel.tcp;

import io.avreen.common.codec.tcp.IMessageHeaderCodec;
import io.avreen.common.codec.tcp.IMessageLenCodec;
import io.avreen.common.context.MessageTypes;
import io.avreen.common.netty.client.NettyClientBase;
import io.avreen.hb.HeaderBodyObject;
import io.netty.channel.ChannelPipeline;

/**
 * The class Header body netty client.
 */
public class HeaderBodyNettyClient extends NettyClientBase<HeaderBodyObject> {
    private IMessageLenCodec messageLenCodec;
    private IMessageHeaderCodec messageHeaderCodec;
    private int maxMessageSize = 9999;
    private boolean allocateDirect = false;
    private String channelID;

    /**
     * Instantiates a new Header body netty client.
     */
    public HeaderBodyNettyClient() {

    }

    /**
     * Instantiates a new Header body netty client.
     *
     * @param host            the host
     * @param port            the port
     * @param messageLenCodec the message len codec
     */
    public HeaderBodyNettyClient(String host, int port, IMessageLenCodec messageLenCodec) {

        this(host, port, messageLenCodec, null);
    }

    /**
     * Instantiates a new Header body netty client.
     *
     * @param host               the host
     * @param port               the port
     * @param messageLenCodec    the message len codec
     * @param messageHeaderCodec the message header codec
     */
    public HeaderBodyNettyClient(String host, int port, IMessageLenCodec messageLenCodec, IMessageHeaderCodec messageHeaderCodec) {
        super(host, port);
        this.messageLenCodec = messageLenCodec;
        this.messageHeaderCodec = messageHeaderCodec;
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
     * Sets channel id.
     *
     * @param channelID the channel id
     */
    public void setChannelID(String channelID) {
        this.channelID = channelID;
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


    /**
     * Build byte to message codec header body message codec handler.
     *
     * @return the header body message codec handler
     */
    public HeaderBodyMessageCodecHandler buildByteToMessageCodec() {
        HeaderBodyMessageCodecHandler messageCodecHandler = new HeaderBodyMessageCodecHandler(channelID == null ? getName() : channelID, this, MessageTypes.Request);
        messageCodecHandler.setMessageLenCodec(messageLenCodec);
        messageCodecHandler.setMessageHeaderCodec(messageHeaderCodec);
        messageCodecHandler.setAllocateDirect(allocateDirect);
        messageCodecHandler.setAllocateDirect(allocateDirect);
        messageCodecHandler.setMaxMessageSize(maxMessageSize);
        return messageCodecHandler;
    }

    @Override
    protected void initService() throws Throwable {
        super.initService();
        if (messageLenCodec == null)
            throw new RuntimeException("can not create message encoder. messageLenCodec is null");

    }


    @Override
    public String getType() {
        return "hb-client";
    }
}
