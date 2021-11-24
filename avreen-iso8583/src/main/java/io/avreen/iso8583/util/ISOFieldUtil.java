package io.avreen.iso8583.util;


import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.packager.api.ISOComponentPackager;
import io.avreen.iso8583.packager.impl.base.ISOMsgBasePackager;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The class Iso field util.
 */
public class ISOFieldUtil {
    /**
     * Echo field.
     *
     * @param request    the request
     * @param response   the response
     * @param echoFields the echo fields
     */
    public static void echoField(ISOMsg request, ISOMsg response, int... echoFields) {
        for (int f : echoFields) {
            if (request.hasField(f)) {
                response.set(f, request.getComponent(f));
            }
        }
    }

    /**
     * Check mandatory field.
     *
     * @param isoMsg the iso msg
     * @param fields the fields
     * @throws InvalidFieldException the invalid field exception
     */
    public static void checkMandatoryField(ISOMsg isoMsg, int... fields)
            throws InvalidFieldException {
        for (int f : fields) {
            if (!isoMsg.hasField(f)) {
                throw new InvalidFieldException(f,
                        IErrorCode.MandetoryFieldNotExist, "required field with protocol field=" + f);
            }

        }
    }

    /**
     * Check same.
     *
     * @param isoMsg1 the iso msg 1
     * @param isoMsg2 the iso msg 2
     * @param fields  the fields
     * @throws InvalidFieldException the invalid field exception
     */
    public static void checkSame(ISOMsg isoMsg1, ISOMsg isoMsg2, int... fields)
            throws InvalidFieldException {
        for (int f : fields) {
            if (isoMsg1.hasField(f) && isoMsg2.hasField(f)) {
                boolean isSame = Arrays.equals(isoMsg1.getBytes(f), isoMsg2.getBytes(f));
                if (!isSame)
                    throw new InvalidFieldException(f,
                            IErrorCode.InvalifFormat, "same field failed with protocol field=" + f + " compare value {" + ISOUtil.hexString(isoMsg1.getBytes(f)) + "," + ISOUtil.hexString(isoMsg2.getBytes(f)) + "}");
            }
        }
    }

    /**
     * Unpack composite iso msg.
     *
     * @param fld   the fld
     * @param bytes the bytes
     * @return the iso msg
     */
    public static ISOMsg unpackComposite(ISOComponentPackager[] fld, byte[] bytes) {
        ISOMsg isoMsg = new ISOMsg();
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        ISOMsgBasePackager.unpack(fld, isoMsg, byteBuffer);
        return isoMsg;
    }

    /**
     * Pack composite byte [ ].
     *
     * @param fld    the fld
     * @param isoMsg the iso msg
     * @return the byte [ ]
     */
    public static byte[] packComposite(ISOComponentPackager[] fld, ISOMsg isoMsg) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(9999);
        int total = ISOMsgBasePackager.pack(fld, isoMsg, byteBuffer);
        byte[] bytes = new byte[total];
        byteBuffer.position(0);
        byteBuffer.get(bytes);
        return bytes;
    }

    /**
     * Check numeric field.
     *
     * @param isoMsg the iso msg
     * @param fields the fields
     * @throws InvalidFieldException the invalid field exception
     */
    public static void checkNumericField(ISOMsg isoMsg, int... fields)
            throws InvalidFieldException {
        for (int f : fields) {
            if (isoMsg.hasField(f)) {
                byte[] rawData;
                rawData = isoMsg.getBytes(f);
                for (int idx = 0; idx < rawData.length; idx++) {
                    if (rawData[idx] < 48 || rawData[idx] > 57) {
                        throw new InvalidFieldException(f,
                                IErrorCode.InavlidFieldFormat, "required numeric field with protocol field=" + f);

                    }
                }
            }
        }
    }

}
