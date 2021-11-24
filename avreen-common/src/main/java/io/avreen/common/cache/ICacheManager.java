package io.avreen.common.cache;

import io.avreen.common.context.CacheException;

/**
 * The interface Cache manager.
 */
public interface ICacheManager {

    /**
     * Put if absent boolean.
     *
     * @param key     the key
     * @param msg     the msg
     * @param timeout the timeout
     * @return the boolean
     * @throws CacheException the cache exception
     */
    boolean putIfAbsent(String key, Object msg, long timeout) throws CacheException;

    /**
     * Put.
     *
     * @param key     the key
     * @param msg     the msg
     * @param timeout the timeout
     */
    void put(String key, Object msg, long timeout);

    /**
     * Remove object.
     *
     * @param key the key
     * @return the object
     */
    Object remove(String key);

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    Object get(String key);

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    default Object get(String key, long renewExpireTime) {
        return get(key);
    }

}
