package io.avreen.iso8583.ltv;

import io.avreen.common.packager.BinaryValuePackager;
import io.avreen.common.packager.IValuePackager;
import io.avreen.iso8583.common.ISOComponent;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.packager.api.ISOComponentPackager;
import io.avreen.iso8583.packager.impl.base.Prefixer;
import io.avreen.iso8583.util.ISOUtil;

import java.nio.ByteBuffer;
import java.util.Map;


/**
 * The class Ltv msg util.
 */
public class LTVMsgUtil {

    /**
     * Instantiates a new Ltv msg util.
     */
    public LTVMsgUtil() {
    }

    /**
     * Unpack to tlv message iso msg.
     *
     * @param byteBuffer  the byte buffer
     * @param lenPrefixer the len prefixer
     * @param tagPackger  the tag packger
     * @param flds        the flds
     * @return the iso msg
     */
    public static ISOMsg unpackToTlvMessage(ByteBuffer byteBuffer, Prefixer lenPrefixer,
                                            ISOComponentPackager tagPackger,
                                            Map<Integer, IValuePackager> flds) {

        ISOMsg msg = new ISOMsg();
        if (byteBuffer.limit() == 0)
            return msg;
        for (; ; ) {

            // BCDPrefixer lenPackager = BCDPrefixer.LLLL;

            int len = lenPrefixer.decodeLength(byteBuffer);
            lenPrefixer.getPackedLength();
            if (len == 0)
                continue;

            ISOComponent<String> component = tagPackger.createComponent();
            //

            int totalTagBytes = tagPackger.unpack(component, byteBuffer);

            Object t = component.getValue();
            int tagValue = -1;
            if (t instanceof String)
                tagValue = Integer.parseInt(t.toString());
            else if (t instanceof byte[]) {
                String hexString = ISOUtil.hexString((byte[]) t);
                tagValue = Integer.parseInt(hexString, 16);
            }
//            component.setFieldNumber(tagValue);

            IValuePackager valuePackger = null;
            if (flds.containsKey(tagValue)) {
                valuePackger = flds
                        .get(tagValue);
            } else
                valuePackger = BinaryValuePackager.INSTANCE;

            Object val = valuePackger.unpack(byteBuffer, len - totalTagBytes);
            if (val instanceof String)
                msg.set(tagValue, (String) val);
            else if (val instanceof byte[]) {
                msg.set(tagValue, (byte[]) val);
            }
            if (byteBuffer.position() >= byteBuffer.limit())
                break;
        }

        // Object len = component.getValue();
        return msg;

    }

    /**
     * Pack to tlv message int.
     *
     * @param byteBuffer  the byte buffer
     * @param msg         the msg
     * @param lenPrefixer the len prefixer
     * @param tagPackger  the tag packger
     * @param flds        the flds
     * @return the int
     */
    public static int packToTlvMessage(ByteBuffer byteBuffer, ISOMsg msg, Prefixer lenPrefixer,
                                       ISOComponentPackager tagPackger,
                                       Map<Integer, IValuePackager> flds) {
        Map fields = msg.getChildren();
        //ByteBuffer  valueBytes = ByteBuffer.allocate(999);
        int totalLen = 0;
        for (Object tag : fields.keySet()) {
            Integer tagKey = Integer.parseInt(tag.toString());
            ISOComponent tagValue = (ISOComponent) fields.get(tag);
            if (tagValue == null)
                continue;
            IValuePackager valuePackger = null;
            if (flds.containsKey(tagKey)) {
                valuePackger = flds.get(tagKey);
            } else
                valuePackger = BinaryValuePackager.INSTANCE;

            Object val = tagValue.getValue();
            if (val == null)
                continue;
            if (val instanceof String) {
                if (((String) val).isEmpty())
                    continue;
            } else if (val instanceof byte[]) {
                byte[] bb2 = (byte[]) val;
                if (bb2.length == 0)
                    continue;

            }
            ISOComponent component = tagPackger.createComponent();
            component.setValue(tagKey.toString());
            int lenLenBytes = lenPrefixer.getPackedLength();

            int curPos = byteBuffer.position();
            byteBuffer.position(curPos + lenLenBytes);
            int tagL = tagPackger.pack(component, byteBuffer);

            byte[] packedBytes = valuePackger.pack(val);
            int totalPacked = packedBytes.length;
            byteBuffer.put(packedBytes);
            int endPos = byteBuffer.position();
            byteBuffer.position(curPos);
            int ll = lenPrefixer.encodeLength(totalPacked + tagL, byteBuffer);
            byteBuffer.position(endPos);
            totalLen += (ll + tagL + totalPacked);
        }
        return totalLen;
    }


}
