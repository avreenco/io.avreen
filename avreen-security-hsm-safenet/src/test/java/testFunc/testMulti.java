package testFunc;

import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.SecurityFunctionMessage;

import java.util.ArrayList;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test multi.
 */
public class testMulti {
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
        testEE0800 testEE0800 = new testEE0800(multiplexer);
        testEE0801 testEE0801 = new testEE0801(multiplexer);

        ArrayList<testBase> allTest = new ArrayList<>();
        allTest.add(testEE0400);
        allTest.add(testEE0602);
        allTest.add(testEE0701);
        allTest.add(testEE0702);
        allTest.add(testEE0800);
        allTest.add(testEE0801);

        long start = System.currentTimeMillis();
        int loopCount = 1000;
        for (int i = 0; i < loopCount; i++) {

            for (int idx = 0; idx < allTest.size(); idx++) {
                // new Thread(allTest.get(idx)).start();
                allTest.get(idx).run();
            }
            //Thread.sleep(1000);
        }
        long end = System.currentTimeMillis();

        System.out.println("total=" + (loopCount * allTest.size()) + "  time in ms=" + (end - start) + " tps=" + (((loopCount * allTest.size()) * 1000) / (end - start)));

        System.in.read();
    }

}
