package io.avreen.examples.core.iso8583.common;


import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.util.ISOUtil;

/**
 * The class Message creator.
 */
public class MessageCreator {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        //System.out.println(ISOMsgLogUtil.dumpELKFormat(null, createRequest(100),ISOMsg.Direction.incoming));
    }

    /**
     * Create request iso msg.
     *
     * @return the iso msg
     */
    public static ISOMsg createRequest() {
        return createRequest(1);
    }

    /**
     * Create request iso msg.
     *
     * @param trace the trace
     * @return the iso msg
     */
    public static ISOMsg createRequest(int trace) {
        String track2 = "621986101234567890=876554644543444";
        return new ISOMsg()
        .setMTI("0200")
        .set(3, "010000")
        .set(11, (trace + 1))
        .set(41, (trace * 100 + 1))
        .set(35, track2)
        .set(42, "HADI JAVAD")
        .set(52, ISOUtil.hex2byte("1234567890123456"))
        .set(63, "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890")
        ;
    }

    /**
     * Create response iso msg.
     *
     * @param request the request
     * @return the iso msg
     */
    public static ISOMsg createResponse(ISOMsg request) {
        ISOMsg m = (ISOMsg) request.clone();
        m.setResponseMTI()
        .set(39, "00")
        .unset(35);
        return m;
    }

}
