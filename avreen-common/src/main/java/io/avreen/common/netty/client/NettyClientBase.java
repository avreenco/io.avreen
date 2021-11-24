package io.avreen.common.netty.client;

import io.avreen.common.log.LoggerDomain;
import io.avreen.common.netty.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.InetSocketAddress;
import java.util.function.Supplier;

/**
 * The class Netty client base.
 *
 * @param <M> the type parameter
 */
//@ManagedResource
public abstract class NettyClientBase<M> extends NettyChannelBase<M> implements NettyClientBaseMXBean {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.client.NettyClientBase");
    /**
     * The Bootstrap.
     */
    protected Bootstrap bootstrap = null;
    private String localIP;
    private int localPort;
    private EventLoopGroup bossGroup = null;
    private int bossGroupNumberThread = 0;
    private String host;
    private int port;
    private String[] alternativeHosts;
    private int[] alternativePorts;
    private boolean closeWhenReadComplete = false;

    /**
     * Instantiates a new Netty client base.
     */
    protected NettyClientBase() {

    }

    /**
     * Instantiates a new Netty client base.
     *
     * @param host the host
     * @param port the port
     */
    protected NettyClientBase(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    protected void setHost(String host) {
        this.host = host;
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

    public String getLocalIP() {
        return localIP;
    }

    /**
     * Sets local ip.
     *
     * @param localIP the local ip
     */
    protected void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public int getLocalPort() {
        return localPort;
    }

    /**
     * Sets local port.
     *
     * @param localPort the local port
     */
    protected void setLocalPort(int localPort) {
        this.localPort = localPort;
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


    @Override
    protected void initService() throws Throwable {
        super.initService();
        if (channelOptionConfiguration == null)
            channelOptionConfiguration = new ChannelOptionConfigurationProperties();
        ChannelOptionConfigurationProperties.applyClientDefault(channelOptionConfiguration);

    }

    @Override
    protected void stopService() throws Throwable {
        closeChannelGroup();
        if (bossGroup != null)
            bossGroup.shutdownGracefully().sync();
    }


    /**
     * Connect channel.
     *
     * @return the channel
     * @throws InterruptedException the interrupted exception
     */
    public Channel connect() throws InterruptedException {
        if (!isRunning())
            start();
        InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
        InetSocketAddress localAddress = null;
        if ((localIP != null && !localIP.isEmpty()) && localPort > 0)
            localAddress = new InetSocketAddress(localIP, localPort);
        ChannelFuture f;
        if (localAddress == null)
            f = bootstrap.connect(remoteAddress).sync();
        else
            f = bootstrap.connect(remoteAddress, localAddress).sync();
        if (f.channel() == null)
            throw new RuntimeException("connect fail because connected channel is null name=[" + getName() + "]");

        //registerChannelInChannelGroup(f.channel());
        return f.channel();
    }

    /**
     * Disconnect.
     *
     * @param channel the channel
     */
    public void disconnect(Channel channel) {
        if (channel == null)
            return;
        channel.deregister();
        channel.pipeline().close();
        channel.closeFuture();
        channel.close();
    }

    protected void startService() {

        bossGroup = ChannelConfigUtil.createEventGroup(transportType, bossGroupNumberThread);
        bootstrap = new Bootstrap();
        final NettyClientBase This = this;
        bootstrap.group(bossGroup)
                .channel(ChannelConfigUtil.getSocketChannelClass(transportType))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        channelOptionConfiguration.mapChannelOptions(ch.config());
                        registerChannelInChannelGroup(ch);
                        if (sessionEventListener != null)
                            ch.pipeline().addFirst(new SessionEventHandlerAdaptor().setSessionEvent(sessionEventListener));
                        ch.pipeline().addFirst(buildIPFilterHandler());

                        if (enableSSL) {
                            if (sslContextBuilder == null) {
                                if (logger.isErrorEnabled())
                                    logger.error("start channel adaptor failed beacuse sslContextBuilder is null name=[{}]", getName());
                                ch.close();
                                throw new RuntimeException("sslContextBuilder is null");
                            }

                            ch.pipeline().addFirst(sslContextBuilder.build().newHandler(ch.alloc()));
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
                        if (closeWhenReadComplete)
                            ch.pipeline().addLast(new AutoCloseAfterReadAdaptor());
                        if (additionalChannelHandlers != null) {
                            for (Supplier<ChannelHandler> channelHandler : additionalChannelHandlers)
                                ch.pipeline().addLast(channelHandler.get());
                        }
                    }
                });


    }

    @Override
    protected void destroyService() {
        super.destroyService();
    }

    private String getHostInfos() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[" + getHost() + ":" + getPort());
        if (alternativeHosts != null && alternativePorts != null) {
            for (int idx = 0; idx < alternativeHosts.length; idx++) {
                stringBuffer.append(",");
                stringBuffer.append(alternativeHosts[idx] + ":" + alternativePorts[idx]);
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /**
     * Check connection channel.
     *
     * @return the channel
     */
    public synchronized Channel checkConnection() {

        if (this.isConnected()) {
            if (logger.isDebugEnabled()) {
                logger.debug("check connection host info={} name=[{}]", getHostInfos(), getName());
            }
            return null;
        }
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("trying to connect host info={} name=[{}]", getHostInfos(), getName());
            }
            Channel connectedChannel = this.connect();
            if (logger.isWarnEnabled()) {
                logger.warn("connection ok  to channel={} name=[{}]", connectedChannel, getName());
            }
            return connectedChannel;
        } catch (Exception ex) {
            if (logger.isErrorEnabled())
                logger.error("fail to connect host info={} name=[{}]", getHostInfos(), getName(), ex);
            return null;
        }
    }

    /**
     * Is close when read complete boolean.
     *
     * @return the boolean
     */
    public boolean isCloseWhenReadComplete() {
        return closeWhenReadComplete;
    }

    /**
     * Sets close when read complete.
     *
     * @param closeWhenReadComplete the close when read complete
     */
    public void setCloseWhenReadComplete(boolean closeWhenReadComplete) {
        this.closeWhenReadComplete = closeWhenReadComplete;
    }

    @Override
    public boolean isConnected() {
        if(closeWhenReadComplete)
            return true;
        return super.isConnected();
    }
}
