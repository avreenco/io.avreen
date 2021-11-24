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
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0200");
        isoMsg.set(3, "010000");
        isoMsg.set(11, (trace + 1));
        isoMsg.set(41, (trace * 100 + 1));

        String track2 = "621986101234567890=876554644543444";
        //byte[] f35P = new String(track2).getBytes();
        //byte[] f35 = ISOUtil.xor(f35P,(byte) 0x12);
        // Arrays.fill(f35P,(byte) 0x00);

        isoMsg.set(35, track2);
        isoMsg.set(42, "HADI JAVAD");
        isoMsg.set(52, ISOUtil.hex2byte("1234567890123456"));
        isoMsg.set(63, "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        isoMsg.recalcBitMap();
        return isoMsg;
    }

    /**
     * Create response iso msg.
     *
     * @param request the request
     * @return the iso msg
     */
    public static ISOMsg createResponse(ISOMsg request) {
        ISOMsg m = (ISOMsg) request.clone();
        m.setResponseMTI();
        //Arrays.fill(request.getCharArray(35),'0');
        m.set(39, "00");
        m.unset(35);
        m.recalcBitMap();
        return m;
    }

}
