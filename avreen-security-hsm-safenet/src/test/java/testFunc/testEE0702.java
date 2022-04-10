package testFunc;

import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.MACAlgorithm;
import org.avreen.security.module.api.SecurityFunctionMessage;
import org.avreen.security.module.api.SecurityUtil;
import org.avreen.security.module.api.request.Request_MAC_VER_FINAL;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test ee 0702.
 */
public class testEE0702 extends testBase {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        testBase testCase = new testEE0702();
        testCase.run();
    }

    /**
     * Instantiates a new Test ee 0702.
     *
     * @param multiplexer the multiplexer
     */
    public testEE0702(IMultiplexer<SecurityFunctionMessage> multiplexer) {
        super(multiplexer);
    }

    /**
     * Instantiates a new Test ee 0702.
     */
    public testEE0702() {
    }

    @Override
    public void run() {
        Request_MAC_VER_FINAL g = new Request_MAC_VER_FINAL();
        g.setFM((byte) 0);
        g.setAlg(MACAlgorithm.ISO_9807);
        g.setICD(SecurityUtil.hex2byte("0000000000000000"));
        g.setMPK_Spec("0001");
        g.setMAC(SecurityUtil.hex2byte("21BD2B7D"));
        g.setData(SecurityUtil.hex2byte("E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"));
        g.buildHeader();
        sender.send(multiplexer, g);

    }
}
