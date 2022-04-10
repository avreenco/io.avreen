package io.avreen.mq.api;

import io.avreen.common.actor.ActorBase;
import io.avreen.common.context.MsgContext;
import io.avreen.common.log.LoggerDomain;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The class Msg publisher abstract.
 *
 * @param <M> the type parameter
 */
public abstract class MsgPublisherAbstract<M> extends ActorBase implements IMsgPublisher<M>, MsgPublisherAbstractMXBean {

    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".reactor.messagebroker.api.MsgPublisherAbstract");
    private AtomicLong tx_pending = new AtomicLong(0);
    private AtomicLong tx = new AtomicLong(0);
    private ConcurrentHashMap<String, AtomicLong> queue_tx = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, AtomicLong> queue_tx_pending = new ConcurrentHashMap<>();


    @Override
    public void publish(String queueName, MsgContext<M> msgContext) {
        if (!isRunning()) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("publish msg failed because publisher is not started message-broker={}", getName());
            return;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("publish to queue name={} message-broker={} and msg={}", queueName, getName(), msgContext.getMsg());
        }
        tx_pending.incrementAndGet();
        AtomicLong atomicLong_tx = StatisticUtil.getCounter(queueName, queue_tx);
        AtomicLong atomicLong_tx_pending = StatisticUtil.getCounter(queueName, queue_tx_pending);
        atomicLong_tx_pending.incrementAndGet();
        publishMsg(queueName, msgContext);
        tx_pending.decrementAndGet();
        atomicLong_tx_pending.decrementAndGet();
        tx.incrementAndGet();
        atomicLong_tx.incrementAndGet();
    }

    /**
     * Publish msg.
     *
     * @param queueName  the queue name
     * @param msgContext the msg context
     */
    public abstract void publishMsg(String queueName, MsgContext<M> msgContext);


    @Override
    public String toString() {
        return "Publisher." + getName();
    }

    @Override
    protected void startService() throws Throwable {

    }

    @Override
    protected void stopService() throws Throwable {

    }

    public String getQueueTx() {
        return StatisticUtil.toString(queue_tx);
    }

    public String getQueueTxPending() {
        return StatisticUtil.toString(queue_tx_pending);
    }

    @Override
    public long getTotalTx() {
        return tx.get();
    }

    @Override
    public long getTotalTxPending() {
        return tx_pending.get();
    }

    @Override
    public void resetStatistics() {
        tx_pending.set(0);
        tx.set(0);
        for (String q : queue_tx.keySet()) {
            queue_tx.get(q).set(0);
        }
    }
}
