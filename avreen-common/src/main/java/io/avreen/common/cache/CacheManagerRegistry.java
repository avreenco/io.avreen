package io.avreen.common.cache;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class Cache manager registry.
 */
public class CacheManagerRegistry {

    /**
     * The constant cacheManagerHashMap.
     */
    public static ConcurrentHashMap<String, ICacheManager> cacheManagerHashMap = new ConcurrentHashMap<>();

    /**
     * Register cache manager.
     *
     * @param name         the name
     * @param msgPublisher the msg publisher
     */
    public static void registerCacheManager(String name, ICacheManager msgPublisher) {
        cacheManagerHashMap.putIfAbsent(name, msgPublisher);
    }

    /**
     * Gets cache manager.
     *
     * @param name the name
     * @return the cache manager
     */
    public static ICacheManager getCacheManager(String name) {
        return getCacheManager(name, true);
    }

    /**
     * Gets cache manager.
     *
     * @param name            the name
     * @param throwIfNotExist the throw if not exist
     * @return the cache manager
     */
    public static ICacheManager getCacheManager(String name, boolean throwIfNotExist) {
        ICacheManager cacheManager = cacheManagerHashMap.get(name);
        if (cacheManager == null && throwIfNotExist)
            throw new RuntimeException("can not found cache manager with name=" + name);
        return cacheManager;
    }

}
