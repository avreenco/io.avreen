package testFunc;

import org.avreen.security.module.api.FunctionCodes;
import org.avreen.security.module.api.MACAlgorithm;
import org.avreen.security.module.api.SecurityUtil;
import org.avreen.security.module.api.request.Request_MAC_GEN_FINAL;
import org.avreen.security.module.api.response.Response_MAC_GEN_FINAL;
import org.avreen.security.module.impl.hsm.safenet.channel.SecurityFunctionMessageRepository;
import org.avreen.security.module.impl.hsm.safenet.engine.SafeNetMsgPackager;

import java.nio.ByteBuffer;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test encode.
 */
public class testEncode {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        System.out.println();
        int total = 8000000;

        for (int idx = 0; idx < total; idx++) {
            if ((idx % (total / 100)) == 0)
                System.out.print("*");
            test(true, false);
        }


        System.out.println();
        long end = System.currentTimeMillis();
        double tps = (double) total / (double) ((end - start));


        System.out.println("OK " + total + " in " + (end - start) + " (ms) time tps=" + (int) (tps * 1000));

    }

    /**
     * Test.
     *
     * @param encodeOnly the encode only
     * @param dump       the dump
     * @throws Exception the exception
     */
    public static void test(boolean encodeOnly, boolean dump) throws Exception {
        Request_MAC_GEN_FINAL g = SecurityFunctionMessageRepository.buildRequestMessage(FunctionCodes.MAC_GEN_FINAL);
        g.setFM((byte) 0);
        g.setAlg(MACAlgorithm.ISO_9807);
        g.setMAClength((byte) 4);
        g.setICD(SecurityUtil.hex2byte("0000000000000000"));
        g.setMPK_Spec("0001");
        g.setData(SecurityUtil.hex2byte("E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"));
        g.buildHeader();
        ByteBuffer byteBuffer = ByteBuffer.allocate(9999);
        int tLen = SafeNetMsgPackager.encode(byteBuffer, g);
        byteBuffer.limit(tLen);
        byteBuffer.rewind();
        Response_MAC_GEN_FINAL r = SecurityFunctionMessageRepository.buildResponseMsg(FunctionCodes.MAC_GEN_FINAL);
        if (dump)
            System.out.println("encode1 message=" + SecurityUtil.hexString(byteBuffer));
        if (encodeOnly)
            return;
        if (dump)
            System.out.println("OK");

    }
}
