package io.avreen.redis.common;

import java.util.HashMap;

/**
 * The class Redis client config registry.
 */
public class RedisClientConfigRegistry {

    /**
     * The constant redisClientConfigHashMap.
     */
    public static HashMap<String, RedisClientConfigProperties> redisClientConfigHashMap = new HashMap<>();

    /**
     * Register redis client config.
     *
     * @param redisClientConfig the redis client config
     */
    public static void registerRedisClientConfig(RedisClientConfigProperties redisClientConfig) {
        redisClientConfigHashMap.putIfAbsent(redisClientConfig.getName(), redisClientConfig);
    }

    /**
     * Gets redis client config.
     *
     * @param name the name
     * @return the redis client config
     */
    public static RedisClientConfigProperties getRedisClientConfig(String name) {
        RedisClientConfigProperties redisClientConfig = redisClientConfigHashMap.get(name);
        if (redisClientConfig == null)
            throw new RuntimeException("can not found redis client config with name=" + name);
        return redisClientConfig;
    }

}