package io.avreen.common.netty;


import io.avreen.common.IMessageKeyProvider;
import io.avreen.common.actor.ActorBase;
import io.avreen.common.cache.ICacheManager;
import io.avreen.common.context.*;
import io.avreen.common.limiter.IRateLimiter;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.ChannelGroupUtil;
import io.avreen.common.util.SimpleToStringUtil;
import io.avreen.common.util.TPS;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
// http example
//http://www.seepingmatter.com/2016/03/30/a-simple-standalone-http-server-with-netty.html

/**
 * The class Netty channel base.
 *
 * @param <M> the type parameter
 */
public abstract class NettyChannelBase<M> extends ActorBase implements
                                                            IMessageCodecListener<M>,
                                                            NettyChannelBaseMXBean
{
    //protected IReactModel<M> reactModel;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.NettyChannelBase");

    /**
     * The Ssl context builder.
     */
    protected SslContextBuilder sslContextBuilder;
    private static AtomicLong nameSeq = new AtomicLong(0);

    private TPS rx_tps = new TPS();
    private TPS tx_tps = new TPS();
    private TPS rx_process_failed_tps = new TPS();

    private ICacheManager cacheManager;

    private String channelId;

    /**
     * The Enable ssl.
     */
    protected boolean enableSSL = false;
    /**
     * The Channel option configuration.
     */
    protected ChannelOptionConfigurationProperties channelOptionConfiguration;
    /**
     * The Transport type.
     */
    protected TransportTypes transportType = TransportTypes.nio;
    /**
     * The Additional channel handlers.
     */
    protected List<Supplier<ChannelHandler>> additionalChannelHandlers;
    private ChannelGroup channelGroup;


    private IChannelGroupRepository channelGroupRepository;

    private IMessageKeyProvider messageKeyProvider;

    /**
     * The Read time out event.
     */
    protected IReadTimeOutEvent readTimeOutEvent;


    private Duration idleTime;
    private Duration readTimeout;

    /**
     * The Session event listener.
     */
    protected ISessionEventListener sessionEventListener;

    /**
     * Sets channel idle listener.
     *
     * @param channelIdleListener the channel idle listener
     */
    public void setChannelIdleListener(IChannelIdleListener channelIdleListener)
    {
        this.channelIdleListener = channelIdleListener;
    }

    /**
     * The Channel idle listener.
     */
    protected IChannelIdleListener channelIdleListener;
    private AtomicLong rx = new AtomicLong(0);
    private AtomicInteger rx_pending = new AtomicInteger(0);
    private AtomicLong tx = new AtomicLong(0);
    private AtomicInteger tx_pending = new AtomicInteger(0);
    private AtomicInteger decodeFailed = new AtomicInteger(0);
    private AtomicInteger encodeFailed = new AtomicInteger(0);
    private int messageExpireSecond = 5;
    private IMsgProcessor<M> msgProcessor;
    private Executor decodeProcessExecutor;
    private volatile RuleBaseAllowDenyIpFilter ruleBaseIpFilter = null;
    private Object initFilterLock = new Object();
    private ArrayList<String> allowIPList;
    private ArrayList<String> denyIPList;
    private IRateLimiter decodeRateLimiter;
    private boolean closeChannelOnDecodeRateCondition = false;
    /**
     * The Global traffic shaping handler.
     */
    protected GlobalTrafficShapingHandler globalTrafficShapingHandler;
    /**
     * The Global channel traffic shaping handler.
     */
    protected GlobalChannelTrafficShapingHandler globalChannelTrafficShapingHandler;
    /**
     * The Traffic configuration.
     */
    protected TrafficConfigurationProperties trafficConfiguration;


    /**
     * Instantiates a new Netty channel base.
     */
    public NettyChannelBase()
    {

    }

    /**
     * Sets cache manager.
     *
     * @param cacheManager the cache manager
     */
    public void setCacheManager(ICacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
    }

    /**
     * Gets cache manager.
     *
     * @return the cache manager
     */
    public ICacheManager getCacheManager()
    {
        return cacheManager;
    }

    /**
     * Gets decode rate limiter.
     *
     * @return the decode rate limiter
     */
    public IRateLimiter getDecodeRateLimiter()
    {
        return decodeRateLimiter;
    }

    public String getDecodeRateLimiterInfo()
    {
        if (decodeRateLimiter == null)
            return "-";
        return decodeRateLimiter.toString();
    }


    /**
     * Sets decode rate limiter.
     *
     * @param decodeRateLimiter the decode rate limiter
     */
    public void setDecodeRateLimiter(IRateLimiter decodeRateLimiter)
    {
        this.decodeRateLimiter = decodeRateLimiter;
    }

    public boolean isCloseChannelOnDecodeRateCondition()
    {
        return closeChannelOnDecodeRateCondition;
    }

    /**
     * Sets close channel on decode rate condition.
     *
     * @param closeChannelOnDecodeRateCondition the close channel on decode rate condition
     */
    public void setCloseChannelOnDecodeRateCondition(boolean closeChannelOnDecodeRateCondition)
    {
        this.closeChannelOnDecodeRateCondition = closeChannelOnDecodeRateCondition;
    }

    /**
     * Build traffic handlers.
     *
     * @param executor the executor
     */
    protected void buildTrafficHandlers(ScheduledExecutorService executor)
    {
        if (trafficConfiguration == null)
            return;

        long totalChannelThroughput = trafficConfiguration.getTotalChannelThroughput();
        long perChannelThroughput = trafficConfiguration.getPerChannelThroughput();
        long throughputCheckInterval = trafficConfiguration.getThroughputCheckInterval()
                                                           .toMillis();
        long trafficCheckMaxTime = trafficConfiguration.getTrafficCheckMaxTime()
                                                       .toMillis();
        if (totalChannelThroughput > 0 && perChannelThroughput > 0)
        {
            globalChannelTrafficShapingHandler = new GlobalChannelTrafficShapingHandler(executor,
                                                                                        totalChannelThroughput,
                                                                                        totalChannelThroughput,
                                                                                        perChannelThroughput,
                                                                                        perChannelThroughput,
                                                                                        throughputCheckInterval,
                                                                                        trafficCheckMaxTime);
            globalTrafficShapingHandler = null;
        } else if (totalChannelThroughput > 0)
        {
            globalTrafficShapingHandler = new GlobalTrafficShapingHandler(executor,
                                                                          totalChannelThroughput,
                                                                          totalChannelThroughput,
                                                                          throughputCheckInterval,
                                                                          trafficCheckMaxTime);
            globalChannelTrafficShapingHandler = null;
        } else
        {
            globalTrafficShapingHandler = null;
            globalChannelTrafficShapingHandler = null;
        }
    }


    /**
     * Gets allow ip list.
     *
     * @return the allow ip list
     */
    public ArrayList<String> getAllowIPList()
    {
        return allowIPList;
    }

    /**
     * Gets idle time.
     *
     * @return the idle time
     */
    public Duration getIdleTime()
    {
        return idleTime;
    }

    public String getIdleTimeString()
    {
        if (idleTime == null)
            return "-";
        return idleTime.toString();
    }

    /**
     * Sets allow ip list.
     *
     * @param allowIPList the allow ip list
     */
    protected void setAllowIPList(ArrayList<String> allowIPList)
    {
        this.allowIPList = allowIPList;
    }

    /**
     * Gets deny ip list.
     *
     * @return the deny ip list
     */
    public ArrayList<String> getDenyIPList()
    {
        return denyIPList;
    }

    /**
     * Sets deny ip list.
     *
     * @param denyIPList the deny ip list
     */
    protected void setDenyIPList(ArrayList<String> denyIPList)
    {
        this.denyIPList = denyIPList;
    }

    /**
     * Gets decode process executor.
     *
     * @return the decode process executor
     */
    public Executor getDecodeProcessExecutor()
    {
        return decodeProcessExecutor;
    }

    /**
     * Sets decode process executor.
     *
     * @param decodeProcessExecutor the decode process executor
     */
    protected void setDecodeProcessExecutor(Executor decodeProcessExecutor)
    {
        this.decodeProcessExecutor = decodeProcessExecutor;
    }

    /**
     * Build ip filter handler rule base allow deny ip filter.
     *
     * @return the rule base allow deny ip filter
     */
    protected RuleBaseAllowDenyIpFilter buildIPFilterHandler()
    {
        if (ruleBaseIpFilter != null)
            return ruleBaseIpFilter;
        synchronized (initFilterLock)
        {
            if (ruleBaseIpFilter != null)
                return ruleBaseIpFilter;
            ArrayList<IpSubnetFilterRule> ipSubnetFilterRules = new ArrayList<>();
            if (allowIPList != null)
                IPFilterUtil.addSource(ipSubnetFilterRules,
                                       allowIPList,
                                       IpFilterRuleType.ACCEPT);
            if (denyIPList != null)
                IPFilterUtil.addSource(ipSubnetFilterRules,
                                       denyIPList,
                                       IpFilterRuleType.REJECT);
            IpFilterRule[] ipFilterRules = new IpFilterRule[ipSubnetFilterRules.size()];
            ipSubnetFilterRules.toArray(ipFilterRules);
            ruleBaseIpFilter = new RuleBaseAllowDenyIpFilter(ipFilterRules);
        }
        return ruleBaseIpFilter;
    }

    /**
     * Sets session event listener.
     *
     * @param sessionEventListener the session event listener
     */
    public void setSessionEventListener(ISessionEventListener sessionEventListener)
    {
        this.sessionEventListener = sessionEventListener;
    }

    /**
     * Init channel group.
     */
    void initChannelGroup()
    {
        if (channelGroup == null)
            channelGroup = new DefaultChannelGroup(getName(),
                                                   GlobalEventExecutor.INSTANCE);
        if (channelGroupRepository != null)
            channelGroupRepository.put(getName(),
                                       channelGroup);
    }


    @Override
    protected void initService() throws
                                 Throwable
    {
        super.initService();
        if (getName() == null)
        {
            String genName = "channel." + nameSeq.incrementAndGet();
            if (logger.isWarnEnabled())
                logger.warn("channel name is null. set name={}",
                            genName);
            setName(genName);
        }
        initChannelGroup();
    }

    public TransportTypes getTransportType()
    {
        return transportType;
    }

    /**
     * Sets transport type.
     *
     * @param transportType the transport type
     */
    protected void setTransportType(TransportTypes transportType)
    {
        this.transportType = transportType;
    }


    /**
     * Sets channel id.
     *
     * @param channelId the channel id
     */
    protected void setChannelId(String channelId)
    {
        this.channelId = channelId;
    }

    /**
     * Gets channel id.
     *
     * @return the channel id
     */
    public String getChannelId()
    {
        return channelId;
    }

    /**
     * Gets traffic configuration.
     *
     * @return the traffic configuration
     */
    public TrafficConfigurationProperties getTrafficConfiguration()
    {
        return trafficConfiguration;
    }

    /**
     * Sets traffic configuration.
     *
     * @param trafficConfiguration the traffic configuration
     */
    protected void setTrafficConfiguration(TrafficConfigurationProperties trafficConfiguration)
    {
        this.trafficConfiguration = trafficConfiguration;
    }

    /**
     * Gets channel option configuration.
     *
     * @return the channel option configuration
     */
    public ChannelOptionConfigurationProperties getChannelOptionConfiguration()
    {
        return channelOptionConfiguration;
    }

    /**
     * Sets channel option configuration.
     *
     * @param channelOptionConfiguration the channel option configuration
     */
    protected void setChannelOptionConfiguration(ChannelOptionConfigurationProperties channelOptionConfiguration)
    {
        this.channelOptionConfiguration = channelOptionConfiguration;
    }

    public boolean anyActiveConnected()
    {
        return ChannelGroupUtil.existAnyActiveConnection(channelGroup);
    }

    /**
     * Gets ssl context builder.
     *
     * @return the ssl context builder
     */
    public SslContextBuilder getSslContextBuilder()
    {
        return sslContextBuilder;
    }

    /**
     * Sets ssl context builder.
     *
     * @param sslContextBuilder the ssl context builder
     */
    protected void setSslContextBuilder(SslContextBuilder sslContextBuilder)
    {
        this.sslContextBuilder = sslContextBuilder;
    }

    public boolean isEnableSSL()
    {
        return enableSSL;
    }

    /**
     * Sets enable ssl.
     *
     * @param enableSSL the enable ssl
     */
    protected void setEnableSSL(boolean enableSSL)
    {
        this.enableSSL = enableSSL;
    }

    public void resetCounters()
    {
        rx.set(0);
        rx_pending.set(0);
        tx.set(0);
        tx_pending.set(0);
        decodeFailed.set(0);
        encodeFailed.set(0);
        rx_tps.reset();
        tx_tps.reset();
    }

    public long getRx()
    {
        return rx.get();
    }

    public String getRxTps()
    {
        return rx_tps.toString();
    }

    public String getRxProcessFailedTps()
    {
        return rx_process_failed_tps.toString();
    }

    public String getTxTps()
    {
        return tx_tps.toString();
    }

    public int getRx_pending()
    {
        return rx_pending.get();
    }

    public long getTx()
    {
        return tx.get();
    }

    public int getTx_pending()
    {
        return tx_pending.get();
    }

    public int getDecodeFailed()
    {
        return decodeFailed.get();
    }

    public int getEncodeFailed()
    {
        return encodeFailed.get();
    }

    /**
     * Init pipeline.
     *
     * @param channelPipeline the channel pipeline
     */
    protected abstract void initPipeline(ChannelPipeline channelPipeline);


    @Override
    public void beforeEncode(ChannelHandlerContext channelHandlerContext,
                             M m)
    {
        tx_pending.incrementAndGet();
    }

    public int getMessageExpireSecond()
    {
        return messageExpireSecond;
    }

    /**
     * Sets message expire second.
     *
     * @param messageExpireSecond the message expire second
     */
    protected void setMessageExpireSecond(int messageExpireSecond)
    {
        this.messageExpireSecond = messageExpireSecond;
    }

    @Override
    public void afterEncode(ChannelHandlerContext channelHandlerContext,
                            M m,
                            Throwable e)
    {
        tx.incrementAndGet();
        tx_tps.tick();
        tx_pending.decrementAndGet();
        if (e != null)
            encodeFailed.incrementAndGet();
    }

    @Override
    protected void destroyService()
    {
        super.destroyService();
    }

    public int getTotalActiveConnections()
    {

        return ChannelGroupUtil.getTotalConnections(channelGroup,
                                                    true);
    }


    public int getTotalConnections()
    {
        return ChannelGroupUtil.getTotalConnections(channelGroup,
                                                    false);
    }

    /**
     * Is connected boolean.
     *
     * @return the boolean
     */
    public boolean isConnected()
    {
        return getTotalActiveConnections() > 0;
    }

    public boolean isRegisteredSessionEventHandler()
    {
        return sessionEventListener != null;
    }

    public List<String> getAllowsIp()
    {
        return allowIPList;
    }

    public String getOptionsString()
    {
        return SimpleToStringUtil.toString(channelOptionConfiguration);
    }

    public List<String> getDeniesIp()
    {
        return denyIPList;
    }


    /**
     * Send channel.
     *
     * @param msg the msg
     * @return the channel
     */
    public Channel send(M msg)
    {

        Channel channel = ChannelGroupUtil.selectChannel(channelGroup);
        if (channel == null)
            return null;
        channel.writeAndFlush(msg);
        return channel;
    }

    /**
     * Sets message key provider.
     *
     * @param messageKeyProvider the message key provider
     */
    public void setMessageKeyProvider(IMessageKeyProvider messageKeyProvider)
    {
        this.messageKeyProvider = messageKeyProvider;
    }

    /**
     * Sets idle time.
     *
     * @param idleTime the idle time
     */
    protected void setIdleTime(Duration idleTime)
    {
        this.idleTime = idleTime;
    }

    /**
     * Gets read timeout.
     *
     * @return the read timeout
     */
    public Duration getReadTimeout()
    {
        return readTimeout;
    }

    public String getReadTimeoutString()
    {
        if (readTimeout == null)
            return "-";
        return readTimeout.toString();
    }

    /**
     * Sets read timeout.
     *
     * @param readTimeout the read timeout
     */
    protected void setReadTimeout(Duration readTimeout)
    {
        this.readTimeout = readTimeout;
    }

    public String getSessions()
    {
        return ChannelGroupUtil.getSessions(channelGroup);
    }

    /**
     * Gets msg processor.
     *
     * @return the msg processor
     */
    public IMsgProcessor<M> getMsgProcessor()
    {
        return msgProcessor;
    }

    /**
     * Sets msg processor.
     *
     * @param msgProcessor the msg processor
     */
    public void setMsgProcessor(IMsgProcessor<M> msgProcessor)
    {
        this.msgProcessor = msgProcessor;
    }

    @Override
    public void beforeDecode(ChannelHandlerContext channelHandlerContext)
    {
        rx_pending.incrementAndGet();
    }

    private void afterDecode_Internal(ChannelHandlerContext channelHandlerContext,
                                      M m,
                                      String channelId,
                                      Throwable e,
                                      Map<String, Object> additionalInfo,
                                      MsgTracer msgTracer)
    {
        boolean doneDecode = false;
        try
        {
            if (m == null)
                return;
            if (decodeRateLimiter != null)
            {
                if (!decodeRateLimiter.tryAcquire("channel."+getName()))
                {
                    logger.error("!!!!!!!! reach rate limit condition.");
                    if (closeChannelOnDecodeRateCondition)
                    {
                        logger.error("closing channel because reach rate limit condition.");
                        channelHandlerContext.close();
                    }
                    return;
                }
            }
            doneDecode = true;
            IMessageTypeSupplier messageTypeSupplier = null;
            if (m instanceof IMessageTypeSupplier)
                messageTypeSupplier = (IMessageTypeSupplier) m;

            MessageTypes messageTypes = MessageTypes.Request;
            if (messageTypeSupplier != null)
                messageTypes = messageTypeSupplier.getMessageTypes();
            MsgContext<M> msgContext = NettyMsgContextUtil.createMsgContext(channelHandlerContext.channel(),
                                                                            getName(),
                                                                            m,
                                                                            msgTracer);
            msgContext.setChannelId(channelId);
            msgContext.setEdgeNodeId(NodeInfo.NODE_ID);
            long captureTime = System.currentTimeMillis();
            if (m instanceof ICaptureTimeSupportObject)
                ((ICaptureTimeSupportObject) m).setCaptureTime(captureTime);
            long expireTime = captureTime + (messageExpireSecond) * 1000;
            msgContext.setCaptureTime(captureTime);
            msgContext.setExpireTime(expireTime);
            if (m instanceof IMsgContextAware)
                ((IMsgContextAware) m).setMsgContext(msgContext);
            if (additionalInfo != null)
            {
                for (String key : additionalInfo.keySet())
                {
                    msgContext.put(key,
                                   additionalInfo.get(key));
                }
            }

            if (MessageTypes.Reject.equals(messageTypes))
            {
                if (logger.isErrorEnabled())
                    logger.error("receive  reject message={}",
                                 m);
            } else
            {
                if (MessageTypes.Request.equals(messageTypes) && messageKeyProvider != null)
                {
                    String msgKey = messageKeyProvider.getKey(m,
                                                              getName(),
                                                              false);
                    try
                    {
                        if (cacheManager != null)
                        {
                            boolean isPut = cacheManager.putIfAbsent(msgKey,
                                                                     Boolean.TRUE,
                                                                     expireTime);
                            if (!isPut)
                                throw new RuntimeException("duplicate transaction with key=" + msgKey);
                        }
                    } catch (Exception ex)
                    {
                        if (logger.isErrorEnabled())
                            logger.error(ex.getMessage(),
                                         e);
                        return;
                    }
                }
            }
            if (msgProcessor != null)
            {
                msgProcessor.process(channelHandlerContext,
                                     msgContext);
            } else
            {
                if (logger.isWarnEnabled())
                    logger.warn("message processor is null. something is wrong. channel name={}",
                                getName());
            }
        } finally
        {
            if (m != null)
            {
                rx.incrementAndGet();
                rx_tps.tick();
                if (!doneDecode)
                {
                    decodeFailed.incrementAndGet();
                    rx_process_failed_tps.tick();
                }
            }
            if (e != null)
                decodeFailed.incrementAndGet();

            rx_pending.decrementAndGet();
        }

    }


    @Override
    public void afterDecode(ChannelHandlerContext channelHandlerContext,
                            String channelID,
                            M m,
                            Throwable e,
                            Map<String, Object> additionalInfo)
    {
        MsgTracer msgTracer = null;
        if (additionalInfo != null && additionalInfo.containsKey(ContextKeyUtil.MSG_TRACE))
            msgTracer = (MsgTracer) additionalInfo.get(ContextKeyUtil.MSG_TRACE);
        if (decodeProcessExecutor == null)
            afterDecode_Internal(channelHandlerContext,
                                 m,
                                 channelID,
                                 e,
                                 additionalInfo,
                                 msgTracer);
        else
        {
            MsgTracer finalMsgTracer = msgTracer;
            decodeProcessExecutor.execute(() ->
                                          {
                                              if (finalMsgTracer != null)
                                                  finalMsgTracer.inject();
                                              afterDecode_Internal(channelHandlerContext,
                                                                   m,
                                                                   channelID,
                                                                   e,
                                                                   additionalInfo,
                                                                   finalMsgTracer);
                                          });

        }
    }

    /**
     * Close channel group.
     */
    protected final void closeChannelGroup()
    {
        if (channelGroup != null)
            channelGroup.close();
    }

    /**
     * Register channel in channel group.
     *
     * @param channel the channel
     */
    protected final void registerChannelInChannelGroup(Channel channel)
    {
        if (channelGroup != null)
            channelGroup.add(channel);
    }

    public String getTrafficInfo()
    {
        if (globalChannelTrafficShapingHandler != null)
            return globalChannelTrafficShapingHandler.toString();
        if (globalTrafficShapingHandler != null)
            return globalTrafficShapingHandler.toString();
        return SimpleToStringUtil.toString(trafficConfiguration);
    }

    /**
     * Sets channel group repository.
     *
     * @param channelGroupRepository the channel group repository
     * @return the channel group repository
     */
    public NettyChannelBase<M> setChannelGroupRepository(IChannelGroupRepository channelGroupRepository)
    {
        if (this.channelGroupRepository != null && channelGroupRepository != null)
        {
            if (this.channelGroupRepository != channelGroupRepository)
                throw new RuntimeException("channel group repository only set onec ");
        }
        this.channelGroupRepository = channelGroupRepository;
        initChannelGroup();
        return this;
    }

    /**
     * Gets channel group repository.
     *
     * @return the channel group repository
     */
    public IChannelGroupRepository getChannelGroupRepository()
    {
        return channelGroupRepository;
    }

    /**
     * Gets additional channel handlers.
     *
     * @return the additional channel handlers
     */
    public List<Supplier<ChannelHandler>> getAdditionalChannelHandlers()
    {
        return additionalChannelHandlers;
    }

    /**
     * Sets additional channel handlers.
     *
     * @param additionalChannelHandlers the additional channel handlers
     * @return the additional channel handlers
     */
    public NettyChannelBase<M> setAdditionalChannelHandlers(List<Supplier<ChannelHandler>> additionalChannelHandlers)
    {
        this.additionalChannelHandlers = additionalChannelHandlers;
        return this;
    }

    /**
     * Gets session event listener.
     *
     * @return the session event listener
     */
    public ISessionEventListener getSessionEventListener()
    {
        return sessionEventListener;
    }

    /**
     * Sets read time out event.
     *
     * @param readTimeOutEvent the read time out event
     */
    public void setReadTimeOutEvent(IReadTimeOutEvent readTimeOutEvent)
    {
        this.readTimeOutEvent = readTimeOutEvent;
    }
}

