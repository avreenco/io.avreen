package testFunc;

import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.SecurityFunctionMessage;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test base.
 */
public abstract class testBase implements Runnable {
    /**
     * The Multiplexer.
     */
    protected IMultiplexer<SecurityFunctionMessage> multiplexer;

    /**
     * Instantiates a new Test base.
     *
     * @param multiplexer the multiplexer
     */
    public testBase(IMultiplexer<SecurityFunctionMessage> multiplexer) {
        this.multiplexer = multiplexer;
    }

    /**
     * Instantiates a new Test base.
     */
    public testBase() {
        this.multiplexer = sender.createMultiplexer();
    }


}
