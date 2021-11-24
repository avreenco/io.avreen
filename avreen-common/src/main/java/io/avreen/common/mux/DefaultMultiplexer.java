package io.avreen.common.mux;


import io.avreen.common.IMessageKeyProvider;
import io.avreen.common.actor.ActorBase;
import io.avreen.common.cache.ICacheManager;
import io.avreen.common.cache.SimpleCacheManager;
import io.avreen.common.context.IMsgContextAware;
import io.avreen.common.context.MsgContext;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.TPS;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The class Default multiplexer.
 *
 * @param <T> the type parameter
 */
public class DefaultMultiplexer<T> extends ActorBase implements IMultiplexer<T>, DefaultMultiplexerMXBean {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.mux.DefaultMultiplexer");

    private IMessageKeyProvider<T> keyProvider;
    private AtomicLong rx = new AtomicLong(0);
    private AtomicLong tx = new AtomicLong(0);
    private AtomicLong rx_expired = new AtomicLong(0);
    private AtomicInteger rx_pending = new AtomicInteger(0);
    private AtomicInteger rx_unhandled = new AtomicInteger(0);
    private int pendingLimit = -1;
    private ICacheManager multiplexerCacheManager;
    private IMultiplexerMsgSender<T> multiplexerMsgSender;


    private TPS requestTPS = new TPS();
    private TPS responseTPS = new TPS();


    /**
     * Instantiates a new Default multiplexer.
     *
     * @param multiplexerMsgSender the multiplexer msg sender
     * @param keyProvider          the key provider
     */
    public DefaultMultiplexer(IMultiplexerMsgSender<T> multiplexerMsgSender, IMessageKeyProvider<T> keyProvider) {
        this.keyProvider = keyProvider;
        this.multiplexerMsgSender = multiplexerMsgSender;
    }

    /**
     * Instantiates a new Default multiplexer.
     */
    public DefaultMultiplexer() {
    }

    /**
     * Gets multiplexer msg sender.
     *
     * @return the multiplexer msg sender
     */
    public IMultiplexerMsgSender<T> getMultiplexerMsgSender() {
        return multiplexerMsgSender;
    }

    /**
     * Sets multiplexer msg sender.
     *
     * @param multiplexerMsgSender the multiplexer msg sender
     */
    public void setMultiplexerMsgSender(IMultiplexerMsgSender<T> multiplexerMsgSender) {
        this.multiplexerMsgSender = multiplexerMsgSender;
    }

    /**
     * Sets multiplexer cache manager.
     *
     * @param multiplexerCacheManager the multiplexer cache manager
     */
    public void setMultiplexerCacheManager(ICacheManager multiplexerCacheManager) {
        this.multiplexerCacheManager = multiplexerCacheManager;
    }


    public int getPendingLimit() {
        return pendingLimit;
    }

    @Override
    public String getRequestTPS() {
        return requestTPS.toString();
    }

    @Override
    public String getResponseTPS() {
        return responseTPS.toString();
    }

    /**
     * Sets pending limit.
     *
     * @param pendingLimit the pending limit
     */
    public void setPendingLimit(int pendingLimit) {
        this.pendingLimit = pendingLimit;
    }

    /**
     * Gets key provider.
     *
     * @return the key provider
     */
    public IMessageKeyProvider<T> getKeyProvider() {
        return keyProvider;
    }

    /**
     * Sets key provider.
     *
     * @param keyProvider the key provider
     */
    public void setKeyProvider(IMessageKeyProvider<T> keyProvider) {
        this.keyProvider = keyProvider;
    }

    public void resetCounters() {
        rx.set(0);
        rx_pending.set(0);
        tx.set(0);
        rx_expired.set(0);
        rx_unhandled.set(0);
        requestTPS.reset();
        responseTPS.reset();
    }

    public long getRx() {
        return rx.get();
    }

    public long getTx() {
        return tx.get();
    }

    public long getRx_expired() {
        return rx_expired.get();
    }

    public int getRx_pending() {
        return rx_pending.get();
    }

    public int getRx_unhandled() {
        return rx_unhandled.get();
    }

    /**
     * Process unhandled.
     *
     * @param m the m
     */
    protected void processUnhandled(T m) {
        rx_unhandled.incrementAndGet();
    }

    public T request(MsgContext<T> msgContext, long timeout) throws MultiplexerException {
        msgContext.clearProcessContext();
        T requestMsg = msgContext.getMsg();
        requestTPS.tick();
        if (pendingLimit > 0) {
            if (rx_pending.get() > pendingLimit) {
                if (logger.isErrorEnabled())
                    logger.error("pending limit reached pending={}   limit={} . name={}", new Object[]{rx_pending.get(), pendingLimit, getName()});
                throw new MultiplexerException(MultiplexerException.QUEUE_FULL, "pending limit reached");
            }
        }
        String key = null;
        if (keyProvider != null)
            key = keyProvider.getKey(requestMsg, getName() + ".out", true);
        else
            key = msgContext.getTracer().getTraceId();

        String req = key + ".req";
        boolean putOk = multiplexerCacheManager.putIfAbsent(req, requestMsg, timeout);
        if (!putOk)
            throw new MultiplexerException(MultiplexerException.DUPLICATE_KEY_EXCEPTION, "duplicate txn req=" + req);
        tx.incrementAndGet();
        multiplexerMsgSender.sendMessage(msgContext, timeout);
        T resp;
        try {
            rx_pending.incrementAndGet();
            resp = waitKey(key, timeout);
            rx_pending.decrementAndGet();
            if (resp == null)
                rx_expired.incrementAndGet();
            else
                rx.incrementAndGet();

        } finally {
        }

        return resp;
    }

    private synchronized T waitKey(String key, long timeout) {
        Object obj;
        long now = System.currentTimeMillis();
        long end = now + timeout;
        synchronized (this) {
            while ((obj = multiplexerCacheManager.remove(key)) == null &&
                    (now = System.currentTimeMillis()) < end) {
                try {
                    this.wait(end - now);
                } catch (InterruptedException e) {
                    if (logger.isErrorEnabled())
                        logger.error("interrupt exception in in method", e);
                }
            }
        }
        return (T) obj;
    }

    public void requestAsync(MsgContext<T> msgContext, long timeout, Object handBack) throws MultiplexerException {
        msgContext.clearProcessContext();
        T requestMsg = msgContext.getMsg();
        requestTPS.tick();
        String key = null;
        if (keyProvider != null)
            key = keyProvider.getKey(requestMsg, getName() + ".out", true);
        else
            key = msgContext.getTracer().getTraceId();
        String req = key + ".req";
        boolean putOk = multiplexerCacheManager.putIfAbsent(req, new MultiplexerAsyncRequest<T>(msgContext, handBack,System.currentTimeMillis()), timeout);
        if (!putOk)
            throw new MultiplexerException(MultiplexerException.DUPLICATE_KEY_EXCEPTION, "duplicate txn req=" + req);

        tx.incrementAndGet();
        multiplexerMsgSender.sendMessage(msgContext, timeout);
    }

    @Override
    public final String getType() {
        return MUX_TYPE;
    }

    protected void initService() {
        if (multiplexerCacheManager == null)
            multiplexerCacheManager = new SimpleCacheManager();
    }

    @Override
    protected void startService() throws Throwable {

    }

    @Override
    protected void stopService() throws Throwable {

    }

    /**
     * Notify response.
     *
     * @param respContext      the resp context
     * @param responseListener the response listener
     */
    public void notifyResponse(MsgContext<T> respContext, IMUXResponseListener<T>  responseListener) {
        T response = respContext.getMsg();
        responseTPS.tick();
        rx.incrementAndGet();
        if (isRunning()) {
            if (response instanceof Boolean) {
                return;
            }
            String key = null;
            if (keyProvider != null)
                key = keyProvider.getKey(response, getName() + ".out", false);
            else
                key = respContext.getTracer().getTraceId();
            String req = key + ".req";

            Object o = multiplexerCacheManager.remove(req);
            if (o != null) {
                if (o instanceof MultiplexerAsyncRequest) {
                    MultiplexerAsyncRequest<T> asyncRequest = (MultiplexerAsyncRequest<T>) o;
                    MsgContext<T> requestContext = asyncRequest.getRequestMsgContext();
                    requestContext.put("$ttl",System.currentTimeMillis()-asyncRequest.getSendMillis());
                    requestContext.mergePassingNode(respContext);
                    if (responseListener == null) {
                        throw new RuntimeException("mux responseListener is null");
                    }
                    Object hansBackObject = asyncRequest.getHandBack();
                    if (hansBackObject instanceof IMsgContextAware) {
                        MsgContext handbackContext = ((IMsgContextAware) hansBackObject).getMsgContext();
                        if (handbackContext != null)
                            handbackContext.mergePassingNode(respContext);
                    }
                    responseListener.onReceive(requestContext, response, hansBackObject);
                } else {
                    multiplexerCacheManager.putIfAbsent(key, response, 60000);
                    synchronized (this) {
                        notifyAll();
                    }
                }
            } else {
                /* process unhandle */
                if (logger.isWarnEnabled())
                    logger.warn("cache item is not valid. may be receive message after timeout ");
            }
        }
        processUnhandled(response);
    }

}
