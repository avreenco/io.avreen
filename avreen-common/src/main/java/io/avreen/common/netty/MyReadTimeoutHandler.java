package io.avreen.common.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

/**
 * The class My read timeout handler.
 */
public class MyReadTimeoutHandler extends ReadTimeoutHandler {
    private IReadTimeOutEvent readTimeOutEvent;

    /**
     * Instantiates a new My read timeout handler.
     *
     * @param timeoutSeconds the timeout seconds
     */
    public MyReadTimeoutHandler(int timeoutSeconds) {
        super(timeoutSeconds);
    }

    /**
     * Instantiates a new My read timeout handler.
     *
     * @param timeout the timeout
     * @param unit    the unit
     */
    public MyReadTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, unit);
    }

    /**
     * Sets read time out event.
     *
     * @param readTimeOutEvent the read time out event
     */
    public void setReadTimeOutEvent(IReadTimeOutEvent readTimeOutEvent) {
        this.readTimeOutEvent = readTimeOutEvent;
    }

    @Override
    protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
        if (readTimeOutEvent != null)
            readTimeOutEvent.readTimedOut(ctx);
        super.readTimedOut(ctx);
    }
}
