package io.avreen.common.cache;


import io.avreen.common.log.LoggerDomain;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The class T space.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 */
@SuppressWarnings("unchecked")
class TSpace<K, V> implements Runnable {
    /**
     * The constant GCDELAY.
     */
    public static final long GCDELAY = 60 * 1000;
    private static InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".common.cache.TSpace");
    private static ScheduledThreadPoolExecutor gcExecutor = newScheduledThreadPoolExecutor();
    private boolean weakValue = false;
    /**
     * The Entries.
     */
    protected ConcurrentHashMap<K, Object> entries;

    /**
     * Instantiates a new T space.
     */
    public TSpace() {
        super();
        entries = new ConcurrentHashMap<>();
        getGCExecutor().scheduleAtFixedRate(this, GCDELAY, GCDELAY, TimeUnit.MILLISECONDS);
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        if (logger.isWarnEnabled())
            logger.warn("space gc finalize!!!!!!");
        getGCExecutor().shutdown();
    }

    /**
     * New scheduled thread pool executor scheduled thread pool executor.
     *
     * @return the scheduled thread pool executor
     */
    public static ScheduledThreadPoolExecutor newScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1,
                r -> {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                });
        stpe.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        stpe.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        return stpe;
    }

    /**
     * Gets gc executor.
     *
     * @return the gc executor
     */
    public static ScheduledThreadPoolExecutor getGCExecutor() {
        return gcExecutor;
    }

    /**
     * Put if absent boolean.
     *
     * @param key     the key
     * @param value   the value
     * @param timeout the timeout
     * @return the boolean
     */
    public boolean putIfAbsent(K key, V value, long timeout) {
        return out(key, value, timeout, true);
    }

    /**
     * Put.
     *
     * @param key     the key
     * @param value   the value
     * @param timeout the timeout
     */
    public void put(K key, V value, long timeout) {
        out(key, value, timeout, false);
    }

    private boolean out(K key, V value, long timeout, boolean putIfAbsent) {
        if (key == null || value == null)
            throw new NullPointerException("key=" + key + ", value=" + value);
        Object v = value;
        if (timeout > 0) {
            v = new Expirable(weakValue ? new WeakReference<>(value) : value, System.currentTimeMillis() + timeout);
        }
        Object oldValue = null;
        if (putIfAbsent)
            oldValue = entries.putIfAbsent(key, v);
        else
            entries.put(key, v);
        if (oldValue != null)
            return false;
        return true;
    }
    public void clear()
    {
        entries.clear();
    }



    /**
     * Get v.
     *
     * @param key the key
     * @return the v
     */
// @Override
    public V get(Object key) {
        return (V) getHead(key, false);
    }

    /**
     * Remove v.
     *
     * @param key the key
     * @return the v
     */
    public V remove(Object key) {
        return (V) getHead(key, true);
    }


    @Override
    public void run() {
        try {
            gc();
        } catch (Exception e) {

            if (logger.isErrorEnabled())
                logger.error("exception in gc method", e);
        }
        if (logger.isInfoEnabled())
            logger.info("space gc call");
    }

    /**
     * Gc.
     */
    public void gc() {
        ConcurrentHashMap.KeySetView<K, Object> keySetView = entries.keySet();
        for (K k : keySetView) {
            get(k);
            Thread.yield();
        }
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }


    private Object getHead(Object key, boolean remove) {
        Object obj;
        if (remove)
            obj = entries.remove(key);
        else
            obj = entries.get(key);
        if (obj instanceof Expirable) {
            Expirable expirable = (Expirable) obj;
            obj = expirable.getValue();
            if (obj instanceof WeakReference)
                obj = ((WeakReference) obj).get();
            if (obj == null)
                entries.remove(key);
        }
        return obj;
//
//        List l = (List) entries.get(key);
//        boolean wasExpirable = false;
//        while (obj == null && l != null && l.size() > 0) {
//            obj = l.get(0);
//            if (obj instanceof Expirable) {
//                obj = ((Expirable) obj).getValue();
//                wasExpirable = true;
//            }
//            if (obj == null) {
//                l.remove(0);
//                if (l.isEmpty()) {
//                    entries.remove(key);
//                }
//            }
//        }
//        if (l != null) {
//            if (remove && obj != null)
//                l.remove(0);
//            if (l.isEmpty()) {
//                entries.remove(key);
//                if (wasExpirable)
//                    unregisterExpirable(key);
//            }
//        }
//        return obj;
    }


    /**
     * The class Expirable.
     */
    static class Expirable implements Comparable, Serializable {

        /**
         * The Serial version uid.
         */
        static final long serialVersionUID = 0xA7F22BF5;

        /**
         * The Value.
         */
        Object value;
        /**
         * The Expires.
         */
        long expires;

        /**
         * Instantiates a new Expirable.
         *
         * @param value   the value
         * @param expires the expires
         */
        public Expirable(Object value, long expires) {
            super();
            this.value = value;
            this.expires = expires;
        }

        /**
         * Is expired boolean.
         *
         * @return the boolean
         */
        public boolean isExpired() {
            return expires < System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return getClass().getName()
                    + "@" + Integer.toHexString(hashCode())
                    + ",value=" + value.toString()
                    + ",expired=" + isExpired();
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public Object getValue() {
            return isExpired() ? null : value;
        }

        @Override
        public int compareTo(Object obj) {
            Expirable other = (Expirable) obj;
            long otherExpires = other.expires;
            if (otherExpires == expires)
                return 0;
            else if (expires < otherExpires)
                return -1;
            else
                return 1;
        }
    }

    /**
     * Is weak value boolean.
     *
     * @return the boolean
     */
    public boolean isWeakValue() {
        return weakValue;
    }

    /**
     * Sets weak value.
     *
     * @param weakValue the weak value
     */
    public void setWeakValue(boolean weakValue) {
        this.weakValue = weakValue;
    }
}
