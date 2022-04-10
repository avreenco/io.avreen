package io.avreen.mq.api;

import io.avreen.common.context.MsgContext;
import io.avreen.common.log.LoggerDomain;
import io.avreen.serializer.ObjectSerializer;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static io.avreen.common.actor.ActorState.STARTED;

/**
 * The class Remote msg publisher abstract.
 *
 * @param <M> the type parameter
 */
public abstract class RemoteMsgPublisherAbstract<M> extends MsgPublisherAbstract<M> implements RemoteMsgPublisherAbstractMXBean {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".reactor.messagebroker.impl.redis.RedisPublisher");
    private boolean bufferEnable = false;
    private int bufferQueueSize = 0;
    private Duration bufferOfferDuration = Duration.ofSeconds(1);
    private int bufferSubscribeHelperThreadCount = 1;
    private BlockingQueue<BufferMessage> bufferQueue;

    /**
     * Sets buffer enable.
     *
     * @param bufferEnable the buffer enable
     */
    public void setBufferEnable(boolean bufferEnable) {
        this.bufferEnable = bufferEnable;
    }

    public int getBufferQueueSize() {
        return bufferQueueSize;
    }

    /**
     * Sets buffer queue size.
     *
     * @param bufferQueueSize the buffer queue size
     */
    public void setBufferQueueSize(int bufferQueueSize) {
        this.bufferQueueSize = bufferQueueSize;
    }

    /**
     * Gets buffer offer duration.
     *
     * @return the buffer offer duration
     */
    public Duration getBufferOfferDuration() {
        return bufferOfferDuration;
    }

    public String getBufferOfferDurationString() {
        if (bufferOfferDuration == null)
            return "-";
        return bufferOfferDuration.toString();
    }

    /**
     * Sets buffer offer duration.
     *
     * @param bufferOfferDuration the buffer offer duration
     */
    public void setBufferOfferDuration(Duration bufferOfferDuration) {
        this.bufferOfferDuration = bufferOfferDuration;
    }

    public int getBufferSubscribeHelperThreadCount() {
        return bufferSubscribeHelperThreadCount;
    }

    /**
     * Sets buffer subscribe helper thread count.
     *
     * @param bufferSubscribeHelperThreadCount the buffer subscribe helper thread count
     */
    public void setBufferSubscribeHelperThreadCount(int bufferSubscribeHelperThreadCount) {
        this.bufferSubscribeHelperThreadCount = bufferSubscribeHelperThreadCount;
    }

    /**
     * Publish bytes.
     *
     * @param queueName  the queue name
     * @param msgContext the msg context
     * @param bytes      the bytes
     */
    protected abstract void publishBytes(String queueName, MsgContext<M>[] msgContext, byte[][] bytes);

    @Override
    public void publishMsg(String queueName, MsgContext<M> msgContext) {
        if (getState() != STARTED) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("publisher not stared");
        }
        byte[] bytes = ObjectSerializer.current().writeObject(msgContext);
        boolean publishBuffer = false;
        if (bufferEnable) {
            try {
                bufferQueue.offer(new BufferMessage(queueName, msgContext, bytes), bufferOfferDuration.toMillis(), TimeUnit.MILLISECONDS);
                publishBuffer = true;
            } catch (Exception e) {
                if (LOGGER.isErrorEnabled())
                    LOGGER.error("publish to buffer fail. continue to direct publish", e);
            }
        }
        if (!publishBuffer) {
            publishBytes(queueName, new MsgContext[]{msgContext}, new byte[][]{bytes});
        }
    }

    @Override
    public boolean isBufferEnable() {
        return bufferEnable;
    }

    @Override
    public String getType() {
        return "RedisPublisher";
    }

    private void subscribeBuffer() {
        while (true) {
            try {
                List<BufferMessage> processList = new ArrayList<>();
                if (bufferQueue.size() == 0) {
                    BufferMessage bufferMessage = bufferQueue.poll(3, TimeUnit.SECONDS);
                    if (bufferMessage != null)
                        processList.add(bufferMessage);
                } else {
                    bufferQueue.drainTo(processList, 10);
                }
                if (!processList.isEmpty()) {
                    HashMap<String, List<BufferMessage>> groupList = new HashMap<>();
                    processList.forEach(bufferMessage -> {
                        List<BufferMessage> list = groupList.get(bufferMessage.queueName);
                        if (list == null)
                            list = new ArrayList<>();
                        list.add(bufferMessage);
                        groupList.put(bufferMessage.queueName, list);
                    });
                    groupList.forEach((s, bufferMessages) -> {
                        byte[][] allBytes = new byte[bufferMessages.size()][];
                        MsgContext[] msgContexts = new MsgContext[bufferMessages.size()];
                        for (int i = 0; i < bufferMessages.size(); i++) {
                            allBytes[i] = bufferMessages.get(i).bytes;
                            msgContexts[i] = bufferMessages.get(i).msgContext;
                        }
                        publishBytes(s, msgContexts, allBytes);
                    });
                }
            } catch (Exception e) {
                if (LOGGER.isErrorEnabled())
                    LOGGER.error("buffer subscribe error", e);
            }
        }
    }

    @Override
    protected void startService() throws Throwable {
        super.startService();
        if (bufferEnable) {
            if (bufferQueueSize > 0)
                bufferQueue = new LinkedBlockingQueue<>(bufferQueueSize);
            else
                bufferQueue = new LinkedBlockingQueue<>();
            for (int idx = 0; idx < bufferSubscribeHelperThreadCount; idx++) {
                new Thread(() -> subscribeBuffer()).start();
            }
        }


    }

    /**
     * The class Buffer message.
     *
     * @param <M> the type parameter
     */
    static class BufferMessage<M> {
        /**
         * The Bytes.
         */
        protected byte[] bytes;
        /**
         * The Queue name.
         */
        protected String queueName;

        /**
         * The Msg context.
         */
        protected MsgContext<M> msgContext;

        /**
         * Instantiates a new Buffer message.
         *
         * @param queueName  the queue name
         * @param msgContext the msg context
         * @param bytes      the bytes
         */
        public BufferMessage(String queueName, MsgContext<M> msgContext, byte[] bytes) {
            this.bytes = bytes;
            this.queueName = queueName;
            this.msgContext = msgContext;
        }

    }


}
