package io.avreen.common.sequence;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Single redis sequence generator.
 */
public interface ISequenceGenerator {
    long generate();

    default long generate(long maxValue) {
        long trace = generate();
        if (maxValue >= 0 && trace > maxValue)
            trace = (int) ((trace % maxValue) + 1);
        return trace;
    }
}
