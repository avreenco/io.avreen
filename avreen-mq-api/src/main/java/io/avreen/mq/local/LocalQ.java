package io.avreen.mq.local;


import io.avreen.common.log.LoggerDomain;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.Enumeration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * The class In memory queue space.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 */
@SuppressWarnings("unchecked")
class LocalQ<K, V> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".reactor.messagebroker.impl.space.InMemoryQueueSpace");
    /**
     * The Entries.
     */
    protected ConcurrentHashMap<K, BlockingQueue<V>> entries;
    private int queuesCapacity = 1000;
    private String name;

    /**
     * Instantiates a new In memory queue space.
     *
     * @param name           the name
     * @param queuesCapacity the queues capacity
     */
    public LocalQ(String name, int queuesCapacity) {
        super();
        this.name = name;
        entries = new ConcurrentHashMap<>();
        this.queuesCapacity = queuesCapacity;
    }

    /**
     * Gets queues capacity.
     *
     * @return the queues capacity
     */
    public int getQueuesCapacity() {
        return queuesCapacity;
    }

    /**
     * Sets queues capacity.
     *
     * @param queuesCapacity the queues capacity
     */
    public void setQueuesCapacity(int queuesCapacity) {
        this.queuesCapacity = queuesCapacity;
    }

    /**
     * Gets active queues.
     *
     * @return the active queues
     */
    public Enumeration<K> getActiveQueues() {
        return entries.keys();
    }

    /**
     * Gets queues size.
     *
     * @return the queues size
     */
    public int getQueuesSize() {
        return entries.size();
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Add boolean.
     *
     * @param key   the key
     * @param value the value
     * @return the boolean
     */
    public boolean add(K key, V value) {
        if (key == null || value == null)
            throw new NullPointerException("key=" + key + ", value=" + value);
        BlockingQueue l = getList(key);
        return l.add(value);
    }

    /**
     * Offer boolean.
     *
     * @param key     the key
     * @param value   the value
     * @param timeout the timeout
     * @param unit    the unit
     * @return the boolean
     * @throws InterruptedException the interrupted exception
     */
    public boolean offer(K key, V value, long timeout, TimeUnit unit) throws InterruptedException {
        if (key == null || value == null)
            throw new NullPointerException("key=" + key + ", value=" + value);
        BlockingQueue l = getList(key);
        if (l == null)
            throw new RuntimeException("BlockingQueue build failed. null pointer exception");
        return l.offer(value, timeout, unit);
    }

    /**
     * Poll v.
     *
     * @param key     the key
     * @param timeout the timeout
     * @return the v
     * @throws InterruptedException the interrupted exception
     */
    public V poll(K key, long timeout) throws InterruptedException {
        BlockingQueue l = getList(key);
        Object o = l.poll(timeout, TimeUnit.MILLISECONDS);
        return (V) o;
    }

    /**
     * Size int.
     *
     * @param key the key
     * @return the int
     */
    public int size(Object key) {
        BlockingQueue l = entries.get(key);
        if (l == null)
            return 0;
        return l.size();
    }

    private BlockingQueue<V> getList(K key) {
        BlockingQueue l = entries.get(key);
        if (l == null) {
            synchronized (this) {
                l = entries.get(key);
                if (l == null) {
                    if (queuesCapacity <= 0)
                        entries.put(key, l = new LinkedBlockingQueue());
                    else
                        entries.put(key, l = new LinkedBlockingQueue(queuesCapacity));
                }
            }
        }
        return l;
    }
}
