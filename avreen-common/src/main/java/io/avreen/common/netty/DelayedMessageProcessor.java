package io.avreen.common.netty;

import io.avreen.common.context.MsgContext;
import io.avreen.common.log.LoggerDomain;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayedMessageProcessor<M> implements IMsgProcessor<M> {
    private Duration delay = Duration.ofSeconds(1);
    private IChannelGroupRepository responseChannelGroupRepository;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.netty.DelayedMessageProcessor");

    public interface IResponseBuilder<M> {
        M buildResponse(M request);
    }


    public Duration getDelay() {
        return delay;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    class DelayObject implements Delayed {
        private ChannelId channelId;
        private long startTime;
        private M request;

        public DelayObject(M request, ChannelId channelId, long delayInMilliseconds) {
            this.request = request;
            this.channelId = channelId;
            this.startTime = System.currentTimeMillis() + delayInMilliseconds;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diff = startTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (
                    this.startTime - ((DelayObject) o).startTime);
        }

        public ChannelId getChannelId() {
            return channelId;
        }

        public M getRequest() {
            return request;
        }
    }


    private BlockingQueue<DelayObject> queue = new DelayQueue();

    private static Channel getSourceChannel(IChannelGroupRepository channelGroupRepository, ChannelId channelId) {
        Channel foundChannel = null;
        for (ChannelGroup channelGroup : channelGroupRepository.getAllChannelGroups()) {
            foundChannel = channelGroup.find(channelId);
            if (foundChannel != null)
                return foundChannel;
        }
        return foundChannel;
    }

    public void startConsume() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        DelayObject object = queue.take();
                        M response = responseBuilder.buildResponse(object.getRequest());
                        Channel sourceChannel = getSourceChannel(responseChannelGroupRepository, object.getChannelId());
                        if (sourceChannel != null)
                            sourceChannel.writeAndFlush(response);
                        else
                            logger.warn("source channel is null or not writeable for send response");
                    } catch (InterruptedException e) {
                        logger.error("error in consume for send response", e);
                    }
                }
            }
        }).start();
    }

    private IResponseBuilder<M> responseBuilder;

    public DelayedMessageProcessor(Duration delay, IResponseBuilder<M> responseBuilder, IChannelGroupRepository responseChannelGroupRepository) {
        this.delay = delay;
        this.responseBuilder = responseBuilder;
        this.responseChannelGroupRepository = responseChannelGroupRepository;
    }

    @Override
    public void process(ChannelHandlerContext channelHandlerContext, MsgContext<M> msg) {
        DelayObject object
                = new DelayObject(msg.getMsg(),
                channelHandlerContext.channel().id(), delay.toMillis());
        try {
            queue.put(object);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
