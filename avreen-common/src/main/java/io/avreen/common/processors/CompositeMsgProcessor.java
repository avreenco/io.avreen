package io.avreen.common.processors;


import io.avreen.common.actor.IDestroySupport;
import io.avreen.common.context.MsgContext;
import io.avreen.common.netty.IMsgProcessor;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * The class Composite msg processor.
 *
 * @param <T> the type parameter
 */
public class CompositeMsgProcessor<T> implements IMsgProcessor<T>, IDestroySupport {
    private List<IMsgProcessor> processorList;

    /**
     * Instantiates a new Composite msg processor.
     */
    public CompositeMsgProcessor() {
    }

    /**
     * Instantiates a new Composite msg processor.
     *
     * @param processorList the processor list
     */
    public CompositeMsgProcessor(List<IMsgProcessor> processorList) {
        this.processorList = processorList;
    }

    /**
     * Gets processor list.
     *
     * @return the processor list
     */
    public List<IMsgProcessor> getProcessorList() {
        return processorList;
    }

    /**
     * Sets processor list.
     *
     * @param processorList the processor list
     */
    public void setProcessorList(List<IMsgProcessor> processorList) {
        this.processorList = processorList;
    }

    @Override
    public void destroy() {
        for (IMsgProcessor processor : processorList) {
            if (processor instanceof IDestroySupport)
                ((IDestroySupport) processor).destroy();
        }
    }

    @Override
    public void process(ChannelHandlerContext channelHandlerContext, MsgContext<T> msg) {
        if (processorList == null)
            return;
        for (IMsgProcessor processor : processorList) {
            processor.process(channelHandlerContext, msg);
        }

    }
}
