package io.avreen.mq.local;

import io.avreen.common.actor.ActorBase;
import io.avreen.common.actor.IDestroySupport;
import io.avreen.common.context.MsgContext;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.TPS;
import io.avreen.mq.api.IMsgConsumer;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The class Space base msg listener adaptor.
 *
 * @param <T> the type parameter
 */
class LocalQListenerAdaptor<T> extends ActorBase implements IDestroySupport, LocalQListenerAdaptorMXBean {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".reactor.messagebroker.impl.space.SpaceBaseMsgListenerAdaptor");
    private LocalQ<String, MsgContext<T>> space;
    private String queueName;
    private Duration waitToIncomeMessageToProcess = Duration.ofSeconds(30);
    private AtomicInteger processing_exception_count = new AtomicInteger(0);
    private Exception lastException = null;
    private TPS tps;

    public String getSubscriberName() {
        return subscriberName;
    }

    @Override
    public String getQueueTps() {
        return tps.toString();
    }

    private String subscriberName;
    private IMsgConsumer<T> msgConsumer;
    private String spaceURI;
    private AtomicLong rx = new AtomicLong(0);
    private Date idleTime;
    private AtomicLong tick = new AtomicLong(0);


    /**
     * Instantiates a new Space base msg listener adaptor.
     *
     * @param subscriberName the subscriber name
     * @param space          the space
     * @param consumer       the consumer
     * @param queueName      the queue name
     * @param tps            the tps
     */
    public LocalQListenerAdaptor(String subscriberName, LocalQ space, IMsgConsumer consumer, String queueName, TPS tps) {
        this.space = space;
        this.spaceURI = space.getName();
        this.msgConsumer = consumer;
        this.queueName = queueName;
        this.subscriberName = subscriberName;
        this.tps = tps;
    }

    /**
     * Gets last exception.
     *
     * @return the last exception
     */
    public Exception getLastException() {
        return lastException;
    }

    /**
     * Gets processing exception count.
     *
     * @return the processing exception count
     */
    public int getProcessing_exception_count() {
        return processing_exception_count.get();
    }

    @Override
    public int getQueuesCapacity() {
        return space.getQueuesCapacity();
    }

    @Override
    public String getSpaceURI() {
        return spaceURI;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    /**
     * Gets wait to income message to process.
     *
     * @return the wait to income message to process
     */
    public Duration getWaitToIncomeMessageToProcess() {
        return waitToIncomeMessageToProcess;
    }

    public String getWaitToProcessString() {
        return waitToIncomeMessageToProcess.toString();
    }

    /**
     * Sets wait to income message to process.
     *
     * @param waitToIncomeMessageToProcess the wait to income message to process
     */
    public void setWaitToIncomeMessageToProcess(Duration waitToIncomeMessageToProcess) {
        this.waitToIncomeMessageToProcess = waitToIncomeMessageToProcess;
    }

    @Override
    public long getRx() {
        return rx.get();
    }

    @Override
    public Date getIdleTime() {
        return idleTime;
    }

    @Override
    public long getTick() {
        return tick.get();
    }


    public void resetCounters() {
        processing_exception_count.set(0);
        tick.set(0);
        rx.set(0);
        tps.reset();
    }

    @Override
    public String getType() {
        return "listener." + subscriberName;
    }

    @Override
    protected void startService() throws Throwable {
        subscribeMessage();
    }

    @Override
    protected void stopService() throws Throwable {
        space.add(queueName, MsgContext.NULL);
    }

    private void subscribeMessage() {
        Thread.currentThread().setName("message-listener-" + subscriberName + "-" + queueName);
        while (isRunning()) {
            boolean start_process = false;
            MsgTracer msgTracer = null;
            try {
                tick.incrementAndGet();
                tps.tick();
                MsgContext<T> msgContext = space.poll(queueName, waitToIncomeMessageToProcess.toMillis());
                if (msgContext != null && msgContext != MsgContext.NULL) {
                    if (msgContext.getMsg() != null) {
                        rx.incrementAndGet();
                        msgTracer = msgContext.getTracer();
                        MsgTracer.inject(msgTracer);
                        if(msgContext.expired())
                        {
                            logger.error("!!!!!!!!!!! receive msg for handle. but expired queue={} expire time={}  msg={}", queueName,msgContext.getExpireTime(),  msgContext.getMsg());
                            continue;
                        }

                        msgConsumer.onMsg(msgContext);
                    }
                } else {
                    idleTime = new Date();
                    if (logger.isWarnEnabled())
                        logger.warn("subscriber is idle space name={} and  qname={}", spaceURI, queueName);

                }
            } catch (Exception e) {
                if (logger.isErrorEnabled())
                    logger.error("exception in qname name={} and process queue qname={}", spaceURI, queueName, e);
                lastException = e;
                processing_exception_count.incrementAndGet();
            } finally {
                if (msgTracer != null)
                    MsgTracer.eject(msgTracer);
            }
        }
        if (logger.isWarnEnabled())
            logger.warn("subscriber stop space name={} and process queue qname={}", spaceURI, queueName);
    }

    @Override
    public synchronized String start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LocalQListenerAdaptor.super.start(true);
            }
        }).start();
        return "StartAsync";
    }

}
