package io.avreen.mq.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The class Statistic util.
 */
public class StatisticUtil {

    /**
     * To string string.
     *
     * @param map the map
     * @return the string
     */
    public static String toString(Map<String, AtomicLong> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String q : map.keySet()) {
            stringBuilder.append("[");
            stringBuilder.append(q);
            stringBuilder.append("=");
            stringBuilder.append(map.get(q));
            stringBuilder.append("]");
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }

    /**
     * Gets counter.
     *
     * @param queueName the queue name
     * @param map       the map
     * @return the counter
     */
    public static AtomicLong getCounter(String queueName, ConcurrentHashMap<String, AtomicLong> map) {
        AtomicLong atomicLong_rx_bytes = map.get(queueName);
        if (atomicLong_rx_bytes == null) {
            atomicLong_rx_bytes = new AtomicLong(0);
            AtomicLong old = map.putIfAbsent(queueName, atomicLong_rx_bytes);
            if (old != null)
                atomicLong_rx_bytes = old;
        }
        return atomicLong_rx_bytes;
    }

    /**
     * Increment counter long.
     *
     * @param queueName the queue name
     * @param map       the map
     * @return the long
     */
    public static long incrementCounter(String queueName, ConcurrentHashMap<String, AtomicLong> map) {

        return getCounter(queueName, map).incrementAndGet();
    }

    /**
     * Add counter long.
     *
     * @param queueName the queue name
     * @param map       the map
     * @param delta     the delta
     * @return the long
     */
    public static long addCounter(String queueName, ConcurrentHashMap<String, AtomicLong> map, long delta) {

        return getCounter(queueName, map).addAndGet(delta);
    }

    /**
     * Decrement counter long.
     *
     * @param queueName the queue name
     * @param map       the map
     * @return the long
     */
    public static long decrementCounter(String queueName, ConcurrentHashMap<String, AtomicLong> map) {

        return getCounter(queueName, map).decrementAndGet();
    }

}
