package org.avreen.security.module.impl.hsm.safenet.channel;

import io.avreen.common.netty.client.NettyClientBase;
import io.netty.channel.ChannelPipeline;
import org.avreen.security.module.api.SecurityFunctionMessage;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Safe net netty client.
 */
public class SafeNetNettyClient extends NettyClientBase<SecurityFunctionMessage> {

    /**
     * Instantiates a new Safe net netty client.
     */
    public SafeNetNettyClient() {

    }

    /**
     * Instantiates a new Safe net netty client.
     *
     * @param host the host
     * @param port the port
     */
    public SafeNetNettyClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void initPipeline(ChannelPipeline channelPipeline) {
        channelPipeline.addLast(buildByteToMessageCodec());
    }



    private SafeNetMsgCodecHandler buildByteToMessageCodec() {

        SafeNetMsgCodecHandler isoMessageDecoderHandler = new SafeNetMsgCodecHandler(this);
        return isoMessageDecoderHandler;
    }


    @Override
    public String getType() {
        return "safenet-client";
    }
}
