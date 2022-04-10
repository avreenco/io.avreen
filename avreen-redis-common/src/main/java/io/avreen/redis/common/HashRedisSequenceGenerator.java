package io.avreen.redis.common;

import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.sync.RedisHashCommands;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Single redis sequence generator.
 */
public class HashRedisSequenceGenerator {
    private RedisHashCommands redisHashCommands;
    private String hashMapName;

    public HashRedisSequenceGenerator(RedisHashCommands redisHashCommands, String hashMapName) {
        this.redisHashCommands = redisHashCommands;
        this.hashMapName = hashMapName;
    }

    public static HashRedisSequenceGenerator buildHashSequenceGenerator(String redisConfigName, String hashMapName) {
        StatefulConnection<String, byte[]> connect = RedisCommandsUtil.connect(RedisClientConfigRegistry.getRedisClientConfig(redisConfigName));
        HashRedisSequenceGenerator redisSequenceGenerator = new HashRedisSequenceGenerator(RedisCommandsUtil.getHashCommands(connect), hashMapName);
        return redisSequenceGenerator;
    }


    public long generate(String key) {
        long trace = redisHashCommands.hincrby(hashMapName, key, 1);
        return trace;
    }

    public boolean replace(String key, long value) {
        return redisHashCommands.hset(hashMapName, key, value);
    }

    public Long get(String key) {
        Object o =  redisHashCommands.hget(hashMapName, key);
        if(o==null)
            return null;
        return (Long) o;
    }

    @Override
    public String toString() {
        return "HashMapRedisSequenceGenerator";
    }
}
