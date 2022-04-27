package io.avreen.common.netty.client;

import io.avreen.common.cache.SimpleCacheManager;
import io.avreen.common.context.IMsgContextAware;
import io.avreen.common.context.ISystemRejectCodes;
import io.avreen.common.context.MsgContext;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.mux.MultiplexerAsyncRequest;
import io.avreen.common.netty.CompositeSessionEventListener;
import io.avreen.common.netty.ISessionEventListener;
import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The class Netty a sync client.
 *
 * @param <M> the type parameter
 */
//@ManagedResource
public class NettyOnDemandASyncClient<M> extends NettyASyncClient<M> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.client.NettyOnDemandASyncClient");

    private long cacheTimeout = 0;

    @Override
    protected void beforeStartClient(NettyClientBase nettyClientBase) {
        nettyClientBase.setCloseWhenReadComplete(true);
    }
    public NettyOnDemandASyncClient(NettyClientBase<M>... nettyClients) {
        super(nettyClients);
    }

    @Override
    protected void beforeStart() {
        if (cacheManager == null) {
            synchronized (this) {
                if (cacheManager == null)
                    cacheManager = new SimpleCacheManager();
            }
        }

        for (NettyClientBase nettyClientBase : nettyClients) {

            if (nettyClientBase.getReadTimeout() != null) {
                long channelReadTimeout = nettyClientBase.getReadTimeout()
                        .toMillis();
                if (channelReadTimeout > cacheTimeout)
                    cacheTimeout = channelReadTimeout;
            }

            { /* add my to processor list */
                if (nettyClientBase.getMsgProcessor() != null)
                    clientMsgProcessor = nettyClientBase.getMsgProcessor();
                nettyClientBase.setMsgProcessor(this);
            }

            { /* add my to event lister list */
                ISessionEventListener sessionEventListener;
                if (nettyClientBase.getSessionEventListener() != null) {
                    List<ISessionEventListener> eventListeners = new ArrayList<>();
                    eventListeners.add(this);
                    eventListeners.add(nettyClientBase.getSessionEventListener());
                    CompositeSessionEventListener compositeSessionEventListener = new CompositeSessionEventListener(eventListeners);
                    sessionEventListener = compositeSessionEventListener;
                } else {
                    sessionEventListener = this;
                }
                nettyClientBase.setSessionEventListener(sessionEventListener);
            }
            nettyClientBase.setReadTimeOutEvent(this);
        }
        if (cacheTimeout == 0)
            cacheTimeout = 60000;
        else
            cacheTimeout += 5000;

    }
    private Channel connectFirst() {
        Exception lastException = null;
        for (NettyClientBase nettyClientBase : nettyClients) {
            try {
                Channel channel = nettyClientBase.connect();
                if (channel != null)
                    return channel;
            } catch (Exception e) {
                if (logger.isWarnEnabled())
                    logger.warn("connect failed.",
                            e);
                lastException = e;
            }
        }
        if (lastException != null)
            throw new RuntimeException("not client found for connect", lastException);
        else
            throw new RuntimeException("not client found for connect");
    }

    @Override
    protected Channel doSend(MsgContext<M> msgContext, Object handback) {
        try {
            Channel channel = connectFirst();
            if (!channel.isOpen() || !channel.isWritable() || !channel.isActive()) {
                if (logger.isWarnEnabled())
                    logger.warn("channel is not active for write ={} message={}",
                            channel,
                            msgContext);
                processReject(null, msgContext, handback, ISystemRejectCodes.DestinationNotReady, -1, "channel is not active");
                return null;
            }
            if (logger.isInfoEnabled())
                logger.info("sending message to to channel={}",
                        channel);
            if (logger.isDebugEnabled())
                logger.debug("sending message ={}",
                        msgContext);
            String req = channel.id()
                    .toString();

            cacheManager.put(req,
                    new MultiplexerAsyncRequest<M>(msgContext,
                            handback, System.currentTimeMillis()),
                    cacheTimeout);
            Object sendMsg = msgContext.getMsg();
            if (sendMsg instanceof IMsgContextAware)
                ((IMsgContextAware) sendMsg).setMsgContext(msgContext);

            channel.writeAndFlush(sendMsg);
            return channel;
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("send error",
                        e);

            processReject(null, msgContext, handback, ISystemRejectCodes.DestinationNotReady, -1, causeException(e).getMessage());
            return null;
        }
    }



}
