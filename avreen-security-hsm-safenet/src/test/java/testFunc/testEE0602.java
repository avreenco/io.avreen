package testFunc;

import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.PinFormat;
import org.avreen.security.module.api.SecurityFunctionMessage;
import org.avreen.security.module.api.SecurityUtil;
import org.avreen.security.module.api.request.Request_PIN_TRAN_2;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test ee 0602.
 */
public class testEE0602 extends testBase {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        testBase testCase = new testEE0602();
        testCase.run();
    }

    /**
     * Instantiates a new Test ee 0602.
     *
     * @param multiplexer the multiplexer
     */
    public testEE0602(IMultiplexer<SecurityFunctionMessage> multiplexer) {
        super(multiplexer);
    }

    /**
     * Instantiates a new Test ee 0602.
     */
    public testEE0602() {
    }

    @Override
    public void run() {
        Request_PIN_TRAN_2 g = new Request_PIN_TRAN_2();
        g.setFM((byte) 0);
        g.setePPKi_PIN(SecurityUtil.hex2byte("30539D10D2A91061"));
        g.setPPKi_Spec("0001");
        g.setPFi(PinFormat.ISO_0);
        g.setANB(SecurityUtil.hex2byte("666666666666"));
        g.setPFo(PinFormat.ISO_0);
        g.setPPKo_Spec("0001");
        sender.send(multiplexer, g);

    }
}
