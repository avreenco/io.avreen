package testFunc;

import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.CipherMode;
import org.avreen.security.module.api.SecurityFunctionMessage;
import org.avreen.security.module.api.SecurityUtil;
import org.avreen.security.module.api.request.Request_DECIPHER_2;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test ee 0801.
 */
public class testEE0801 extends testBase {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        testBase testCase = new testEE0801();
        testCase.run();
    }

    /**
     * Instantiates a new Test ee 0801.
     *
     * @param multiplexer the multiplexer
     */
    public testEE0801(IMultiplexer<SecurityFunctionMessage> multiplexer) {
        super(multiplexer);
    }

    /**
     * Instantiates a new Test ee 0801.
     */
    public testEE0801() {
    }

    @Override
    public void run() {
        Request_DECIPHER_2 g = new Request_DECIPHER_2();
        g.setFM((byte) 0);
        g.setCM(CipherMode.CBC);
        g.seteDPK_Data(SecurityUtil.hex2byte("7DF612A4C3A156D6"));
        g.setICV(SecurityUtil.hex2byte("0000000000000000"));
        g.setDPK_Spec("0001");
        sender.send(multiplexer, g);

    }
}
