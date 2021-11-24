package io.avreen.common.netty.client;

import io.avreen.common.actor.IActor;
import io.avreen.common.context.MsgContext;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.netty.IMsgProcessor;
import io.avreen.common.processors.CompositeMsgProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The class Netty sync client.
 *
 * @param <M> the type parameter
 */
public  class NettySyncClient<M> implements IMsgProcessor<M> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.client.NettySyncClient");
    private M response;
    private Object waitObject = new Object();
    private NettyClientBase<M> nettyClientBase;

    /**
     * Instantiates a new Netty sync client.
     *
     * @param nettyClientBase the netty client base
     */
    public NettySyncClient(NettyClientBase<M> nettyClientBase) {
        this.nettyClientBase = nettyClientBase;

        IMsgProcessor<M> msgProcessor = null;
        if (nettyClientBase.getMsgProcessor() != null) {
            List<IMsgProcessor> msgProcessorList = new ArrayList<>();
            msgProcessorList.add(nettyClientBase.getMsgProcessor());
            msgProcessorList.add(this);
            CompositeMsgProcessor<M> compositeMsgProcessor = new CompositeMsgProcessor<>();
            compositeMsgProcessor.setProcessorList(msgProcessorList);
            msgProcessor = compositeMsgProcessor;
        } else {
            msgProcessor = this;
        }
        nettyClientBase.setMsgProcessor(msgProcessor);
    }

    /**
     * Send m.
     *
     * @param msg     the msg
     * @param timeout the timeout
     * @return the m
     */
    public M send(M msg, long timeout) {
        if (nettyClientBase.getState() == IActor.STARTED)
            nettyClientBase.start();
        Channel   senChannel =  nettyClientBase.send(msg);
        if(senChannel==null)
            throw new RuntimeException("send channel not found for send message. maybe not call connect method");
        synchronized (waitObject) {
            try {
                waitObject.wait(timeout);
            } catch (InterruptedException e) {
                return null;
            }
        }
        return response;
    }

    /**
     * Connect channel.
     *
     * @return the channel
     * @throws Exception the exception
     */
    public Channel connect() throws Exception {
        return nettyClientBase.connect();
    }

    /**
     * Disconnect.
     *
     * @param channel the channel
     */
    public void disconnect(Channel channel) {
        nettyClientBase.disconnect(channel);
    }

    @Override
    public void process(ChannelHandlerContext channelHandlerContext, MsgContext<M> msg) {
        M m = msg.getMsg();
        if (m == null)
            return;
        response = m;
        synchronized (waitObject) {
            waitObject.notifyAll();
        }

    }
}
