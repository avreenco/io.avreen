package io.avreen.common.netty.server;

import io.avreen.common.log.LoggerDomain;
import io.avreen.common.netty.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.function.Supplier;
// http example
//http://www.seepingmatter.com/2016/03/30/a-simple-standalone-http-server-with-netty.html

/**
 * The class Netty server base.
 *
 * @param <M> the type parameter
 */
public abstract class NettyServerBase<M> extends NettyChannelBase<M> implements
        IMessageCodecListener<M>, NettyServerBaseMXBean {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.server.NettyServerBase");

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    private int port = -1;
    private ChannelFuture channelFuture;
    private Thread startThread = null;
    private String inetHost = "0.0.0.0";
    private int bossGroupNumberThread = 0;
    /* number of thread that concurrenct consume message */
    private int workerGroupNumberThread = 0;

    /**
     * Instantiates a new Netty server base.
     */
    public NettyServerBase() {

    }

    /**
     * Instantiates a new Netty server base.
     *
     * @param port the port
     */
    public NettyServerBase(int port) {
        this.port = port;
    }


    /**
     * Gets inet host.
     *
     * @return the inet host
     */
    public String getInetHost() {
        return inetHost;
    }

    /**
     * Sets inet host.
     *
     * @param inetHost the inet host
     */
    protected void setInetHost(String inetHost) {
        this.inetHost = inetHost;
    }

    public int getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    protected void setPort(int port) {
        this.port = port;
    }

    @Override
    public synchronized String start() {
        startThread = new Thread(() -> NettyServerBase.super.start(true));
        startThread.start();
        return "StartAsync";
    }

    public int getBossGroupNumberThread() {
        return bossGroupNumberThread;
    }

    /**
     * Sets boss group number thread.
     *
     * @param bossGroupNumberThread the boss group number thread
     */
    protected void setBossGroupNumberThread(int bossGroupNumberThread) {
        this.bossGroupNumberThread = bossGroupNumberThread;
    }

    public int getWorkerGroupNumberThread() {
        return workerGroupNumberThread;
    }

    /**
     * Sets worker group number thread.
     *
     * @param workerGroupNumberThread the worker group number thread
     */
    protected void setWorkerGroupNumberThread(int workerGroupNumberThread) {
        this.workerGroupNumberThread = workerGroupNumberThread;
    }


    @Override
    protected synchronized void startService() throws Throwable {
        bossGroup = ChannelConfigUtil.createEventGroup(transportType, bossGroupNumberThread);
        workerGroup = ChannelConfigUtil.createEventGroup(transportType, workerGroupNumberThread);
        ServerBootstrap serverBootstrap = new ServerBootstrap(); // (2)
        try {
            buildTrafficHandlers(workerGroup);
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(ChannelConfigUtil.getServerSocketChannelClass(transportType)) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            channelOptionConfiguration.mapChannelOptions(ch.config());
                            registerChannelInChannelGroup(ch);
                            if (sessionEventListener != null) {
                                buildIPFilterHandler();
                                ch.pipeline().addFirst(new SessionEventHandlerAdaptor().setSessionEvent(sessionEventListener));
                            }
                            ch.pipeline().addFirst(buildIPFilterHandler());
                            if (trafficConfiguration != null) {
                                long totalChannelThroughput = trafficConfiguration.getTotalChannelThroughput();
                                long perChannelThroughput = trafficConfiguration.getPerChannelThroughput();
                                long throughputCheckInterval = trafficConfiguration.getThroughputCheckInterval().toMillis();
                                long trafficCheckMaxTime = trafficConfiguration.getTrafficCheckMaxTime().toMillis();
                                if (totalChannelThroughput > 0 && perChannelThroughput > 0) {
                                    ch.pipeline().addLast(globalChannelTrafficShapingHandler);
                                } else if (totalChannelThroughput > 0) {
                                    ch.pipeline().addLast(globalTrafficShapingHandler);
                                } else if (perChannelThroughput > 0) {
                                    ch.pipeline().addLast(new ChannelTrafficShapingHandler(perChannelThroughput, perChannelThroughput, throughputCheckInterval, trafficCheckMaxTime));
                                }
                            }
                            if (enableSSL) {
                                if (sslContextBuilder == null) {
                                    if (logger.isErrorEnabled())
                                        logger.error("start server failed because sslContextBuilder is null and  stoped name=[{}] port={} ", getName(), getPort());
                                    ch.close();
                                    throw new RuntimeException("sslContextBuilder is null");
                                }
                                SslHandler sslHandler = sslContextBuilder.build().newHandler(ch.alloc());
                                ch.pipeline().addFirst(sslHandler);
                            }
                            if (getIdleTime() != null && getIdleTime().getSeconds() > 0) {
                                ch.pipeline().addLast(new IdleStateHandler(0, 0, (int) getIdleTime().getSeconds()) {
                                    @Override
                                    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
                                        super.channelIdle(ctx, evt);
                                        if (logger.isWarnEnabled())
                                            logger.warn("channel idle with id={} name=[{}]", ctx.channel().toString(), getName());
                                        if (channelIdleListener != null)
                                            channelIdleListener.onChannelIdle(ctx);
                                    }
                                });
                            }
                            if (getReadTimeout() != null && getReadTimeout().getSeconds() > 0) {
                                MyReadTimeoutHandler myReadTimeoutHandler = new MyReadTimeoutHandler((int) getReadTimeout().getSeconds());
                                myReadTimeoutHandler.setReadTimeOutEvent(readTimeOutEvent);
                                ch.pipeline().addLast(myReadTimeoutHandler);
                            }
                            initPipeline(ch.pipeline());
                            if (additionalChannelHandlers != null) {
                                for (Supplier<ChannelHandler> channelHandler : additionalChannelHandlers)
                                    ch.pipeline().addLast(channelHandler.get());
                            }

                        }
                    });
            ChannelFuture f;
            f = serverBootstrap.bind(inetHost, port).sync();
            if (logger.isWarnEnabled())
                logger.warn("listen on port {} name=[{}]", port, getName());

            channelFuture = f;
            channelFuture = f.channel().closeFuture();
            channelFuture.sync();

        } finally {
            if (logger.isErrorEnabled())
                logger.error("stop server  on  port {} name=[{}]", port, getName());
            try {

            } catch (Exception e) {
                if (logger.isErrorEnabled())
                    logger.error("error in shutdown  on  port {} name=[{}]", port, getName(), e);

            }

        }

    }

    @Override
    protected void initService() throws Throwable {
        super.initService();
        if (port <= 0)
            throw new RuntimeException("invalid port number for listen. port=" + port);
        if (channelOptionConfiguration == null)
            channelOptionConfiguration = new ChannelOptionConfigurationProperties();
        ChannelOptionConfigurationProperties.applyServerDefault(channelOptionConfiguration);

    }

    @Override
    protected void stopService() throws Throwable {
        if (workerGroup != null)
            workerGroup.shutdownGracefully().sync();
        if (bossGroup != null)
            bossGroup.shutdownGracefully().sync();
    }

    @Override
    protected String getNotificationUserData() {
        return getInetHost() == null ? "0.0.0.0" : getInetHost() + ":" + getPort();
    }


}

