package testFunc;

import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.*;
import org.avreen.security.module.api.request.Request_IT_KEY_GEN;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test ee 0400.
 */
public class testEE0400 extends testBase {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        testBase testCase = new testEE0400();
        testCase.run();
    }

    /**
     * Instantiates a new Test ee 0400.
     *
     * @param multiplexer the multiplexer
     */
    public testEE0400(IMultiplexer<SecurityFunctionMessage> multiplexer) {
        super(multiplexer);
    }

    /**
     * Instantiates a new Test ee 0400.
     */
    public testEE0400() {
    }

    @Override
    public void run() {
        Request_IT_KEY_GEN g = new Request_IT_KEY_GEN();
        g.setFM((byte) 0);
        KeyFlag keyFlag = new KeyFlag();
        keyFlag.setCipherMode(CipherMode.CBC);
        keyFlag.setKeyLength(KeyLength.DES);
        keyFlag.setKeyType(KeyTypes.BDK);
        g.setKey_Flags(keyFlag);
        g.setKTM_Spec("0001");
        sender.send(multiplexer, g);
    }
}
