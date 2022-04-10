package io.avreen.redis.common;

import io.avreen.common.actor.ActorBaseMXBean;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Single redis sequence generator.
 */
public interface RedisSequenceGeneratorMXBean extends ActorBaseMXBean {

    String setSequenceValue(long val);

    long getCurrentValue();

    long increment();
}
