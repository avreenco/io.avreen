package io.avreen.common.limiter;

import java.util.HashMap;

/**
 * The class Rate limiter registry.
 */
public class RateLimiterRegistry {

    /**
     * The constant redisClientConfigHashMap.
     */
    public static HashMap<String, IRateLimiter> redisClientConfigHashMap = new HashMap<>();

    /**
     * Register rate limiter.
     *
     * @param name              the name
     * @param redisClientConfig the redis client config
     */
    public static void registerRateLimiter(String name, IRateLimiter redisClientConfig) {
        redisClientConfigHashMap.putIfAbsent(name, redisClientConfig);
    }

    /**
     * Gets rate limiter.
     *
     * @param name the name
     * @return the rate limiter
     */
    public static IRateLimiter getRateLimiter(String name) {
        IRateLimiter redisClientConfig = redisClientConfigHashMap.get(name);
        if (redisClientConfig == null)
            throw new RuntimeException("can not found rate limiter with name=" + name);
        return redisClientConfig;
    }

}