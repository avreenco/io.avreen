package io.avreen.common.processors;


import io.avreen.common.actor.IDestroySupport;
import io.avreen.common.context.MsgContext;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.netty.IMsgProcessor;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * The class Concurrent msg processor.
 *
 * @param <T> the type parameter
 */
public class ConcurrentMsgProcessor<T> implements IMsgProcessor<T>, IDestroySupport {
    private IMsgProcessor<T> realProcessor;
    private ExecutorService executor;

    /**
     * Instantiates a new Concurrent msg processor.
     */
    public ConcurrentMsgProcessor() {
    }

    /**
     * Instantiates a new Concurrent msg processor.
     *
     * @param realProcessor      the real processor
     * @param threadPoolExecutor the thread pool executor
     */
    public ConcurrentMsgProcessor(IMsgProcessor<T> realProcessor, ExecutorService threadPoolExecutor) {
        this.realProcessor = realProcessor;
        this.executor = threadPoolExecutor;
    }

    /**
     * Gets real processor.
     *
     * @return the real processor
     */
    public IMsgProcessor getRealProcessor() {
        return realProcessor;
    }

    /**
     * Sets real processor.
     *
     * @param realProcessor the real processor
     */
    public void setRealProcessor(IMsgProcessor realProcessor) {
        this.realProcessor = realProcessor;
    }

    /**
     * Sets executor.
     *
     * @param executor the executor
     */
    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void destroy() {
        if (executor != null)
            executor.shutdownNow();
        if (realProcessor instanceof IDestroySupport)
            ((IDestroySupport) realProcessor).destroy();

    }

    @Override
    public void process(ChannelHandlerContext channelHandlerContext, MsgContext<T> msg) {
        executor.execute(() -> {
            MsgTracer.inject(msg.getTracer());
            try {
                realProcessor.process(channelHandlerContext, msg);
            } finally {
                MsgTracer.eject(msg.getTracer());
            }

        });
    }
}
