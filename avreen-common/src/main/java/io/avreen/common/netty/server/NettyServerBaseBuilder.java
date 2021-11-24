package io.avreen.common.netty.server;

import io.avreen.common.netty.NettyChannelBaseBuilder;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The class Netty server base builder.
 *
 * @param <T> the type parameter
 * @param <M> the type parameter
 * @param <B> the type parameter
 */
public class NettyServerBaseBuilder<T extends NettyServerBase, M, B extends NettyServerBaseBuilder>
        extends NettyChannelBaseBuilder<T, M, B> {

    /**
     * The Netty server.
     */
    protected T nettyServer;
    private B This;
    private static AtomicLong nameSeq = new AtomicLong(0);

    /**
     * Instantiates a new Netty server base builder.
     *
     * @param nettyServer the netty server
     */
    protected NettyServerBaseBuilder(T nettyServer) {
        super(nettyServer);
        this.nettyServer = nettyServer;
        This = (B) this;

    }

    /**
     * Inet host b.
     *
     * @param inetHost the inet host
     * @return the b
     */
    public B inetHost(String inetHost) {
        nettyServer.setInetHost(inetHost);
        return This;
    }

    /**
     * Port b.
     *
     * @param port the port
     * @return the b
     */
    public B port(int port) {
        nettyServer.setPort(port);
        return This;
    }

    /**
     * Boss group threads b.
     *
     * @param bossGroupNumberThread the boss group number thread
     * @return the b
     */
    public B bossGroupThreads(int bossGroupNumberThread) {
        nettyServer.setBossGroupNumberThread(bossGroupNumberThread);
        return This;
    }

    /**
     * Worker group threads b.
     *
     * @param workerGroupNumberThread the worker group number thread
     * @return the b
     */
    public B workerGroupThreads(int workerGroupNumberThread) {
        nettyServer.setWorkerGroupNumberThread(workerGroupNumberThread);
        return This;
    }

    public T build() {
        String name = delegate.getName();
        if (name == null) {
            name = "iso_netty_server." + nameSeq.incrementAndGet();
            delegate.setName(name);
        }
        super.build();
        if (nettyServer.getPort() < 0)
            nettyServer.setPort(1391);
        return nettyServer;
    }
}

