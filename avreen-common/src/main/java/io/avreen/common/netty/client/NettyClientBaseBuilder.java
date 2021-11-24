package io.avreen.common.netty.client;

import io.avreen.common.cache.ICacheManager;
import io.avreen.common.netty.NettyChannelBaseBuilder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The class Netty client base builder.
 *
 * @param <T> the type parameter
 * @param <M> the type parameter
 * @param <B> the type parameter
 */
public class NettyClientBaseBuilder<T extends NettyClientBase, M, B extends NettyClientBaseBuilder>
        extends NettyChannelBaseBuilder<T, M, B> {
    private B This;
    private String localIP;
    private int localPort;
    private static AtomicLong nameSeq = new AtomicLong(0);

    /**
     * Local ip b.
     *
     * @param localIP the local ip
     * @return the b
     */
    public B localIp(String localIP) {
        delegate.setLocalIP(localIP);
        return This;
    }

    /**
     * Local port b.
     *
     * @param localPort the local port
     * @return the b
     */
    public B localPort(int localPort) {
        delegate.setLocalPort(localPort);
        return This;
    }

    /**
     * Instantiates a new Netty client base builder.
     *
     * @param nettyServer the netty server
     */
    protected NettyClientBaseBuilder(T nettyServer) {
        super(nettyServer);
        this.delegate = nettyServer;
        This = (B) this;

    }

    /**
     * Host b.
     *
     * @param host the host
     * @return the b
     */
    public B host(String host) {
        delegate.setHost(host);
        return This;
    }

    public B cacheManager(ICacheManager cacheManager) {
        delegate.setCacheManager(cacheManager);
        return This;
    }

    /**
     * Port b.
     *
     * @param port the port
     * @return the b
     */
    public B port(int port) {
        delegate.setPort(port);
        return This;
    }

    /**
     * Boss group threads b.
     *
     * @param bossGroupNumberThread the boss group number thread
     * @return the b
     */
    public B bossGroupThreads(int bossGroupNumberThread) {
        delegate.setBossGroupNumberThread(bossGroupNumberThread);
        return This;
    }

    public T build() {
        String name = delegate.getName();
        if (name == null) {
            name = "iso_netty_client." + nameSeq.incrementAndGet();
            delegate.setName(name);
        }
        super.build();
        if (delegate.getPort() < 0)
            delegate.setPort(1391);
        return delegate;
    }
}

