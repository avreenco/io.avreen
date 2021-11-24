package io.avreen.common.netty.client;

import io.avreen.common.cache.ICacheManager;
import io.avreen.common.cache.SimpleCacheManager;
import io.avreen.common.context.*;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.mux.IMUXResponseListener;
import io.avreen.common.mux.MultiplexerAsyncRequest;
import io.avreen.common.netty.*;
import io.avreen.common.util.ChannelGroupUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.SocketAddress;
import java.time.Duration;
import java.util.*;

/**
 * The class Netty a sync client.
 *
 * @param <M> the type parameter
 */
//@ManagedResource
public class NettyASyncClient<M> implements IMsgProcessor<M>,
        IReadTimeOutEvent,
        ISessionEventListener {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.client.NettyASyncClient");

    protected List<NettyClientBase<M>> nettyClients = new ArrayList<>();
    private Duration reconnectDelay = Duration.ofSeconds(10);
    private Timer reconnectTimer;
    private boolean started = false;
    private IChannelGroupRepository applyChannelGroupRepository = null;
    private ConnectionModel connectionModel = ConnectionModel.Permanent;
    private ICacheManager cacheManager;
    protected IMUXResponseListener<M> responseListener;
    private long cacheTimeout = 0;
    private IMsgProcessor clientMsgProcessor;


    /**
     * Instantiates a new Netty a sync client.
     *
     * @param nettyClients the netty clients
     */
    public NettyASyncClient(NettyClientBase<M>... nettyClients) {
        for (NettyClientBase clientBase : nettyClients) {
            addClient(clientBase);
        }
    }

    /**
     * Gets clients.
     *
     * @return the clients
     */
    public Iterator<NettyClientBase<M>> getClients() {
        return nettyClients.iterator();
    }

    /**
     * Sets cache manager.
     *
     * @param cacheManager the cache manager
     */
    public void setCacheManager(ICacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }


    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isConnected() {
        if (!started)
            start();
        for (NettyClientBase nettyClientBase : nettyClients) {
            if (nettyClientBase.isConnected())
                return true;
        }
        return false;
    }

    /**
     * Add client.
     *
     * @param clientBase the client base
     */
    public void addClient(NettyClientBase<M> clientBase) {
        this.nettyClients.add(clientBase);
    }

    /**
     * Check different clients channel groups channel group repository.
     *
     * @return the channel group repository
     */
    public IChannelGroupRepository checkDifferentClientsChannelGroups() {
        IChannelGroupRepository last = null;
        int index = 0;
        for (NettyClientBase nettyClientBase : nettyClients) {
            IChannelGroupRepository clientChannelGroupRepository = nettyClientBase.getChannelGroupRepository();
            if (clientChannelGroupRepository != last && index > 0)
                throw new RuntimeException("all client must have same channel group repository");
            last = clientChannelGroupRepository;
            index++;
        }
        return last;
    }

    /**
     * Is null channel groups boolean.
     *
     * @return the boolean
     */
    public boolean isNullChannelGroups() {
        boolean isNull = true;
        for (NettyClientBase nettyClientBase : nettyClients) {
            isNull = isNull & (nettyClientBase.getChannelGroupRepository() == null);
        }
        return true;
    }

    /**
     * Sets reconnect delay.
     *
     * @param reconnectDelay the reconnect delay
     */
    public void setReconnectDelay(Duration reconnectDelay) {
        this.reconnectDelay = reconnectDelay;
    }


    /**
     * Start.
     */
    public synchronized void start() {
        if (started)
            return;

        applyChannelGroupRepository = checkDifferentClientsChannelGroups();

        if (applyChannelGroupRepository == null)
            applyChannelGroupRepository = new ChannelGroupRepository();
        for (NettyClientBase nettyClientBase : nettyClients) {
            nettyClientBase.setChannelGroupRepository(applyChannelGroupRepository);
            if (connectionModel.equals(ConnectionModel.OnDemand))
                nettyClientBase.setCloseWhenReadComplete(true);
            nettyClientBase.start();
        }
        if (connectionModel.equals(ConnectionModel.OnDemand)) {
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
                    IMsgProcessor<M> msgProcessor = null;
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

        if (reconnectTimer == null && connectionModel.equals(ConnectionModel.Permanent)) {

            reconnectTimer = new Timer(true);
            reconnectTimer.scheduleAtFixedRate(new TimerTask() {
                                                   @Override
                                                   public void run() {
                                                       checkConnection();
                                                   }
                                               },
                    0,
                    reconnectDelay.toMillis());
        }

        started = true;
    }
    protected Throwable causeException(Throwable e) {
        if (e.getCause() == null)
            return e;
        return causeException(e.getCause());
    }

    /**
     * Send channel.
     *
     * @param msgContext the msg context
     * @param handback   the handback
     * @return the channel
     */
    public Channel send(MsgContext<M> msgContext,
                        Object handback) {
        if (!started)
            start();
        if (connectionModel.equals(ConnectionModel.OnDemand)) {
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
        } else {
            Object sendMsg = msgContext.getMsg();
            if (sendMsg instanceof IMsgContextAware)
                ((IMsgContextAware) sendMsg).setMsgContext(msgContext);
            return ChannelGroupUtil.writeAndFlush(applyChannelGroupRepository,
                    sendMsg);
        }
    }

    /**
     * Send channel.
     *
     * @param msg the msg
     * @return the channel
     */
    public Channel send(M msg) {

        return send(new MsgContext<M>("client",
                        msg),
                null);
    }

    /**
     * Send channel.
     *
     * @param msg      the msg
     * @param handBack the hand back
     * @return the channel
     */
    public Channel send(M msg,
                        Object handBack) {

        return send(new MsgContext<M>("client",
                        msg),
                handBack);
    }

    private synchronized void checkConnection() {

        for (NettyClientBase nettyClientBase : nettyClients) {
            nettyClientBase.checkConnection();
        }
    }

    /**
     * Sets response listener.
     *
     * @param responseListener the response listener
     */
    public void setResponseListener(IMUXResponseListener<M> responseListener) {
        this.responseListener = responseListener;
    }


    private Channel connectFirst() {
        Exception lastException=null;
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

    /**
     * Sets connection model.
     *
     * @param connectionModel the connection model
     */
    public void setConnectionModel(ConnectionModel connectionModel) {
        this.connectionModel = connectionModel;
    }

    @Override
    public void readTimedOut(ChannelHandlerContext ctx) {
        {
            String req = ctx.channel()
                    .id()
                    .toString();
            Object o = cacheManager.remove(req);
            if (o != null && o instanceof MultiplexerAsyncRequest) {
                MultiplexerAsyncRequest<M> asyncRequest = (MultiplexerAsyncRequest) o;
                processReject(ctx, asyncRequest, ISystemRejectCodes.MessageRoutTimeout, "timeout transaction");
            } else {
                /* process unhandle */
                if (logger.isWarnEnabled())
                    logger.warn("cache item is not valid. may be receive message after timeout ");
            }
        }

    }

    /**
     * Check inactive abnormally.
     *
     * @param eventName the event name
     * @param channel   the channel
     * @param e         the e
     */
    public void checkInactiveAbnormally(ChannelEventTypes eventName,
                                        Channel channel,
                                        Throwable e) {
        if (eventName.equals(ChannelEventTypes.channelInactive)) {
            if (e != null) {
                if (logger.isErrorEnabled())
                    logger.error(e);
            }
            AttributeKey<Boolean> closeManual = AttributeKey.valueOf("normalClose");
            if (closeManual != null && channel.hasAttr(closeManual)) {
                if (logger.isInfoEnabled())
                    logger.info("channel inactive normally channel={}",
                            channel);
                return;
            }
            {
                String req = channel.id()
                        .toString();
                Object o = cacheManager.remove(req);
                if (o != null && o instanceof MultiplexerAsyncRequest) {
                    MultiplexerAsyncRequest<M> asyncRequest = (MultiplexerAsyncRequest) o;
                    processReject(null, asyncRequest, ISystemRejectCodes.ChannelCloseAbnormally, "channel close abnormally");
                    try {
                        channel.close();
                    } catch (Exception ex) {
                        if (logger.isErrorEnabled())
                            logger.error("error in close inactive channel",
                                    e);
                    }
                }
            }

        }
    }

    @Override
    public void fire(ChannelEventTypes eventName,
                     Channel channel,
                     Throwable e) {
        checkInactiveAbnormally(eventName,
                channel,
                e);
    }

    @Override
    public void fire(ChannelEventTypes eventName,
                     SocketAddress remoteAddress,
                     SocketAddress localAddress,
                     Throwable e) {
        /* nothing */
    }

    private void notifyResponse(ChannelHandlerContext channelHandlerContext,
                                MsgContext<M> responseContext) {
        {
            Channel channel = channelHandlerContext.channel();
            M response = responseContext.getMsg();
            if (response instanceof Boolean) {
                return;
            }
            String req = channel.id()
                    .toString();
            Object o = cacheManager.remove(req);
            if (o != null && o instanceof MultiplexerAsyncRequest) {
                MultiplexerAsyncRequest<M> asyncRequest = (MultiplexerAsyncRequest) o;
                MsgTracer.inject(asyncRequest.getRequestMsgContext()
                        .getTracer());
                processResponse(channelHandlerContext, asyncRequest, response);
            } else {
                /* process unhandle */
                if (logger.isWarnEnabled())
                    logger.warn("cache item is not valid. may be receive message after timeout key={} msg={}",
                            req,
                            response);
            }
        }
    }


    @Override
    public void process(ChannelHandlerContext channelHandlerContext,
                        MsgContext<M> msg) {
        notifyResponse(channelHandlerContext,
                msg);
    }

    private void processResponse(ChannelHandlerContext ctx, MultiplexerAsyncRequest<M> asyncRequest, M response) {

        processResponse(ctx, asyncRequest.getRequestMsgContext(), asyncRequest.getHandBack(), response, asyncRequest.getSendMillis());
    }


    private void processResponse(ChannelHandlerContext ctx, MsgContext requestMsgContext, Object handback, M response, long sendMillis) {
        if (sendMillis > 0)
            requestMsgContext.put("$ttl", System.currentTimeMillis() - sendMillis);
        if (requestMsgContext.getTracer() != null)
            MsgTracer.inject(requestMsgContext
                    .getTracer());
        if (response instanceof IMsgContextAware)
            ((IMsgContextAware) response).setMsgContext(requestMsgContext);
        if (responseListener != null)
            responseListener.onReceive(requestMsgContext,
                    response,
                    handback);
        if (clientMsgProcessor != null) {
            clientMsgProcessor.process(ctx,
                    new MsgContext(response, requestMsgContext));
        }

    }

    protected void processReject(ChannelHandlerContext ctx, MsgContext<M> requestMsgContext, Object handback, int rejectCode, long sendMillis, String message) {
        M request = requestMsgContext.getMsg();

        if (request instanceof IRejectSupportObject)
            ((IRejectSupportObject) request).setRejectCode(rejectCode);
        else {
            logger.error("reject message in.but message not support reject set reject code={}", rejectCode);
            return;
        }
        processResponse(ctx, requestMsgContext, handback, request, sendMillis);
    }

    private void processReject(ChannelHandlerContext ctx, MultiplexerAsyncRequest<M> asyncRequest, int rejectCode, String message) {

        processReject(ctx, asyncRequest.getRequestMsgContext(), asyncRequest.getHandBack(), rejectCode, asyncRequest.getSendMillis(), message);
    }


}
