package io.avreen.redis.common;

import io.avreen.common.actor.ActorBase;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.sync.RedisStringCommands;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Single redis sequence generator.
 */
public class RedisSequenceGenerator extends ActorBase implements RedisSequenceGeneratorMXBean {
    private int bufferSize = 20;
    private BlockingQueue<Long> bufferBlockingDeque;
    private RedisStringCommands<String, byte[]> redisStringCommands;
    private String sequenceName;
    private AtomicBoolean init = new AtomicBoolean(false);

    /**
     * Instantiates a new Single redis sequence generator.
     *
     * @param redisStringCommands the redis string commands
     * @param sequenceName        the sequence name
     */
    public RedisSequenceGenerator(RedisStringCommands redisStringCommands, String sequenceName) {
        this(redisStringCommands, sequenceName, 20);
    }

    /**
     * Instantiates a new Single redis sequence generator.
     *
     * @param redisStringCommands the redis string commands
     * @param sequenceName        the sequence name
     * @param bufferSize          the buffer size
     */
    public RedisSequenceGenerator(RedisStringCommands redisStringCommands, String sequenceName, int bufferSize) {
        this.redisStringCommands = redisStringCommands;
        this.sequenceName = sequenceName;
        this.bufferSize = bufferSize;
        bufferBlockingDeque = new ArrayBlockingQueue<>(bufferSize);
    }

    public static RedisSequenceGenerator buildSequenceGenerator(String redisConfigName, String sequenceName, int bufferSize) {
        StatefulConnection<String, byte[]> connect = RedisCommandsUtil.connect(RedisClientConfigRegistry.getRedisClientConfig(redisConfigName));
        RedisSequenceGenerator redisSequenceGenerator = new RedisSequenceGenerator(RedisCommandsUtil.getStringCommands(connect), sequenceName, bufferSize);
        redisSequenceGenerator.setName(sequenceName);
        redisSequenceGenerator.start();
        return redisSequenceGenerator;

    }


    public long generate() {
        init();
        if (bufferBlockingDeque.size() == 0) {
            synchronized (this) {
                if (bufferBlockingDeque.size() == 0) {
                    long newTrace = redisStringCommands.incrby(sequenceName, bufferSize);
                    for (int idx = 0; idx < bufferSize; idx++) {
                        bufferBlockingDeque.add(newTrace - bufferSize + idx);
                    }
                }
            }
        }
        long trace = bufferBlockingDeque.remove();
        return trace;
    }


    private void init() {
        if (init.get())
            return;
        synchronized (this) {
            if (init.get())
                return;
            bufferBlockingDeque = new ArrayBlockingQueue<>(bufferSize);
            init.set(true);
        }
    }

    @Override
    protected void startService() throws Throwable {
        init();
    }

    @Override
    protected void stopService() throws Throwable {

    }

    @Override
    public String toString() {
        return "RedisSequenceGenerator";
    }

    @Override
    public String getType() {
        return "RedisSequence";
    }


    @Override
    public String setSequenceValue(long val) {
        try {
            byte[] valBytes = ((Long) (val)).toString().getBytes();
            return redisStringCommands.set(sequenceName, valBytes);
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    @Override
    public long getCurrentValue() {
        byte[] seq = redisStringCommands.get(sequenceName);
        if (seq == null)
            return 0;
        return Long.parseLong(new String(seq));
    }

    @Override
    public long increment() {
        redisStringCommands.incrby(sequenceName, bufferSize);
        return getCurrentValue();
    }
}
