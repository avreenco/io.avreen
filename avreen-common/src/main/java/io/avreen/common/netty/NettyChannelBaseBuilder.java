package io.avreen.common.netty;

import io.avreen.common.IMessageKeyProvider;
import io.avreen.common.cache.ICacheManager;
import io.avreen.common.limiter.IRateLimiter;
import io.avreen.common.log.LoggerDomain;
import io.netty.channel.ChannelHandler;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * The class Netty channel base builder.
 *
 * @param <T> the type parameter
 * @param <M> the type parameter
 * @param <B> the type parameter
 */
public class NettyChannelBaseBuilder<T extends NettyChannelBase, M, B extends NettyChannelBaseBuilder> {

    /**
     * The Delegate.
     */
    protected T delegate;
    private B This;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.NettyChannelBaseBuilder");

    /**
     * Instantiates a new Netty channel base builder.
     *
     * @param delegate the delegate
     */
    protected NettyChannelBaseBuilder(T delegate) {
        this.delegate = delegate;
        This = (B) this;

    }

    private static AtomicInteger actoreNameIndex = new AtomicInteger(0);
    private ArrayList<Supplier<ChannelHandler>> additionalChannelHandlers = new ArrayList<>();

    /**
     * Channel group repository b.
     *
     * @param channelGroupRepository the channel group repository
     * @return the b
     */
    public B channelGroupRepository(IChannelGroupRepository channelGroupRepository) {
        delegate.setChannelGroupRepository(channelGroupRepository);
        return This;
    }

    /**
     * Allow ip list b.
     *
     * @param allowIPList the allow ip list
     * @return the b
     */
    public B allowIPList(ArrayList<String> allowIPList) {
        delegate.setAllowIPList(allowIPList);
        return This;
    }

    /**
     * Idle time b.
     *
     * @param idleTime the idle time
     * @return the b
     */
    public B idleTime(Duration idleTime) {
        delegate.setIdleTime(idleTime);
        return This;
    }

    /**
     * Channel id b.
     *
     * @param channelId the channel id
     * @return the b
     */
    public B channelId(String channelId) {
        delegate.setChannelId(channelId);
        return This;
    }

    /**
     * Read timeout b.
     *
     * @param readTimeout the read timeout
     * @return the b
     */
    public B readTimeout(Duration readTimeout) {
        delegate.setReadTimeout(readTimeout);
        return This;
    }

    /**
     * Decode rate limiter b.
     *
     * @param rateLimiter the rate limiter
     * @return the b
     */
    public B decodeRateLimiter(IRateLimiter rateLimiter) {
        delegate.setDecodeRateLimiter(rateLimiter);
        return This;
    }

    /**
     * Close channel on decode rate condition b.
     *
     * @param closed the closed
     * @return the b
     */
    public B closeChannelOnDecodeRateCondition(boolean closed) {
        delegate.setCloseChannelOnDecodeRateCondition(closed);
        return This;
    }

    /**
     * Add additional channel handler b.
     *
     * @param channelHandler the channel handler
     * @return the b
     */
    public B addAdditionalChannelHandler(Supplier<ChannelHandler> channelHandler) {
        additionalChannelHandlers.add(channelHandler);
        return This;
    }

    /**
     * Deny ip list b.
     *
     * @param denyIPList the deny ip list
     * @return the b
     */
    public B denyIPList(ArrayList<String> denyIPList) {
        delegate.setDenyIPList(denyIPList);
        return This;
    }

    /**
     * Decode process executor b.
     *
     * @param msgProcessorExecutor the msg processor executor
     * @return the b
     */
    public B decodeProcessExecutor(Executor msgProcessorExecutor) {
        delegate.setDecodeProcessExecutor(msgProcessorExecutor);
        return This;
    }

    /**
     * Cache manager b.
     *
     * @param cacheManager the cache manager
     * @return the b
     */
    public B cacheManager(ICacheManager cacheManager) {
        delegate.setCacheManager(cacheManager);
        return This;
    }

    /**
     * Session event listener b.
     *
     * @param sessionEvent the session event
     * @return the b
     */
    public B sessionEventListener(ISessionEventListener sessionEvent) {
        delegate.setSessionEventListener(sessionEvent);
        return This;
    }

    /**
     * Channel idle listener b.
     *
     * @param channelIdleListener the channel idle listener
     * @return the b
     */
    public B channelIdleListener(IChannelIdleListener channelIdleListener) {
        delegate.setChannelIdleListener(channelIdleListener);
        return This;
    }

    /**
     * Transport type b.
     *
     * @param transportType the transport type
     * @return the b
     */
    public B transportType(TransportTypes transportType) {
        delegate.setTransportType(transportType);
        return This;
    }

    /**
     * Traffic configuration b.
     *
     * @param trafficConfiguration the traffic configuration
     * @return the b
     */
    public B trafficConfiguration(TrafficConfigurationProperties trafficConfiguration) {
        delegate.setTrafficConfiguration(trafficConfiguration);
        return This;
    }

    /**
     * Channel options b.
     *
     * @param channelOptionConfiguration the channel option configuration
     * @return the b
     */
    public B channelOptions(ChannelOptionConfigurationProperties channelOptionConfiguration) {
        delegate.setChannelOptionConfiguration(channelOptionConfiguration);
        return This;
    }

    /**
     * Ssl context builder b.
     *
     * @param sslContextBuilder the ssl context builder
     * @return the b
     */
    public B sslContextBuilder(SslContextBuilder sslContextBuilder) {
        delegate.setSslContextBuilder(sslContextBuilder);
        return This;
    }

    /**
     * Enable ssl b.
     *
     * @param enableSSL the enable ssl
     * @return the b
     */
    public B enableSSL(boolean enableSSL) {
        delegate.setEnableSSL(enableSSL);
        return This;
    }

    /**
     * Message expire second b.
     *
     * @param messageExpireSecond the message expire second
     * @return the b
     */
    public B messageExpireSecond(int messageExpireSecond) {
        delegate.setMessageExpireSecond(messageExpireSecond);
        return This;
    }

    /**
     * Processor b.
     *
     * @param msgProcessor the msg processor
     * @return the b
     */
    public B processor(IMsgProcessor<M> msgProcessor) {
        delegate.setMsgProcessor(msgProcessor);
        return This;
    }

    /**
     * Message key provider b.
     *
     * @param messageKeyProvider the message key provider
     * @return the b
     */
    public B messageKeyProvider(IMessageKeyProvider<M> messageKeyProvider) {
        delegate.setMessageKeyProvider(messageKeyProvider);
        return This;
    }

    /**
     * Enable jmx b.
     *
     * @param enableJmx the enable jmx
     * @return the b
     */
    public B enableJmx(boolean enableJmx) {
        delegate.setEnableJmx(enableJmx);
        return This;
    }

    /**
     * Name netty channel base builder.
     *
     * @param name the name
     * @return the netty channel base builder
     */
    public NettyChannelBaseBuilder name(String name) {
        delegate.setName(name);
        return This;
    }

    /**
     * Build t.
     *
     * @return the t
     */
    public T build() {
        String name = delegate.getName();
        if (name == null) {
            name = "iso_netty_channel." + actoreNameIndex.incrementAndGet();
            delegate.setName(name);
        }
        if (delegate.isEnableSSL() && delegate.getSslContextBuilder() == null)
            throw new RuntimeException("Ssl Context Builder is null");
        if (delegate.getMsgProcessor() == null) {
            if (logger.isWarnEnabled())
                logger.warn("message processor is null for actor name={}");
        }
        delegate.setAdditionalChannelHandlers(additionalChannelHandlers);
        //delegate.setExtraPipelineInit();
        return delegate;
    }

}

