package io.avreen.common.netty.client;

import io.avreen.common.context.IMsgContextAware;
import io.avreen.common.context.ISystemRejectCodes;
import io.avreen.common.context.MsgContext;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.ChannelGroupUtil;
import io.netty.channel.Channel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The class Netty a sync client.
 *
 * @param <M> the type parameter
 */
//@ManagedResource
public class NettyPermanentASyncClient<M> extends NettyASyncClient<M> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.client.NettyPermanentASyncClient");

    private Duration reconnectDelay = Duration.ofSeconds(10);
    private boolean autoConnectOnStartup = true;
    private Timer reconnectTimer;
    public NettyPermanentASyncClient(NettyClientBase<M>... nettyClients) {
        super(nettyClients);
    }

    /**
     * Sets reconnect delay.
     *
     * @param reconnectDelay the reconnect delay
     */
    public void setReconnectDelay(Duration reconnectDelay) {
        this.reconnectDelay = reconnectDelay;
    }

    public void setAutoConnectOnStartup(boolean autoConnectOnStartup) {
        this.autoConnectOnStartup = autoConnectOnStartup;
    }

    @Override
    protected void beforeStart() {
        if (autoConnectOnStartup)
            startKeepAliveConnection();
    }

    private synchronized void startKeepAliveConnection() {
        if (reconnectTimer == null) {
            reconnectTimer = new Timer(true);
            reconnectTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    checkConnection();
                }
            }, 0, reconnectDelay.toMillis());
        }

    }

    /**
     * Send channel.
     *
     * @param msgContext the msg context
     * @param handback   the handback
     * @return the channel
     */
    protected Channel doSend(MsgContext<M> msgContext,
                             Object handback) {
        if (!autoConnectOnStartup) {
            if (reconnectTimer == null) {
                startKeepAliveConnection();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    /* nothing*/
                }
            }
        }
        Object sendMsg = msgContext.getMsg();
        if (sendMsg instanceof IMsgContextAware)
            ((IMsgContextAware) sendMsg).setMsgContext(msgContext);
        Channel channel = ChannelGroupUtil.writeAndFlush(applyChannelGroupRepository,
                sendMsg);
        if (channel == null) {
            if (logger.isWarnEnabled())
                logger.warn("channel is not active for write ={} message={}",
                        channel,
                        msgContext);
            processReject(null, msgContext, handback, ISystemRejectCodes.DestinationNotReady, -1, "channel is not active");
        }
        return channel;
    }

    private synchronized void checkConnection() {

        for (NettyClientBase nettyClientBase : nettyClients) {
            nettyClientBase.checkConnection();
        }
    }


}
