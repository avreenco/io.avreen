package testFunc;

import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.SecurityFunctionMessage;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test single.
 */
public class testSingle {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {

        IMultiplexer<SecurityFunctionMessage> multiplexer = sender.createMultiplexer();
        testEE0400 testEE0400 = new testEE0400(multiplexer);
        testEE0602 testEE0602 = new testEE0602(multiplexer);
        testEE0701 testEE0701 = new testEE0701(multiplexer);
        testEE0702 testEE0702 = new testEE0702(multiplexer);
        testEE0801 testEE0801 = new testEE0801(multiplexer);
        testEE0800 testEE0800 = new testEE0800(multiplexer);

        testEE0400.run();
        testEE0602.run();
        testEE0701.run();
        testEE0702.run();
        testEE0800.run();
        testEE0801.run();

    }

}
