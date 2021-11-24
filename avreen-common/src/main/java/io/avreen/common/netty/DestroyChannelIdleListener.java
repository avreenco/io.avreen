package io.avreen.common.netty;

import io.avreen.common.log.LoggerDomain;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * The class Destroy channel idle listener.
 */
public class DestroyChannelIdleListener implements IChannelIdleListener {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.DestroyChannelIdleListener");


    private static DestroyChannelIdleListener instance = new DestroyChannelIdleListener();

    private DestroyChannelIdleListener() {

    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static DestroyChannelIdleListener getInstance() {
        return instance;
    }


    @Override
    public void onChannelIdle(ChannelHandlerContext ctx) {
        if (logger.isWarnEnabled())
            logger.warn("destroy channel={} for idle strategy", ctx.channel());
        try {
            ctx.channel().close();
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("error close channel={}", ctx.channel(), e);
        }
    }
}

