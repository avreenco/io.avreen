package io.avreen.iso8583.mapper.impl;

import io.avreen.common.util.CodecUtil;
import io.avreen.iso8583.mapper.impl.base.StringValueCodec;

import java.nio.ByteBuffer;

/**
 * The class Hex interpreter.
 */
public class HEXValueCodec implements StringValueCodec {
    /**
     * The constant LEFT_PADDED.
     */
    public static final HEXValueCodec LEFT_PADDED = new HEXValueCodec(true, false);
    /**
     * The constant RIGHT_PADDED.
     */
    public static final HEXValueCodec RIGHT_PADDED = new HEXValueCodec(false, false);
    /**
     * The constant RIGHT_PADDED_F.
     */
    public static final HEXValueCodec RIGHT_PADDED_F = new HEXValueCodec(false, true);
    /**
     * The constant LEFT_PADDED_F.
     */
    public static final HEXValueCodec LEFT_PADDED_F = new HEXValueCodec(true, true);

    private boolean leftPadded;
    private boolean fPadded;

    private HEXValueCodec(boolean leftPadded, boolean fPadded) {
        this.leftPadded = leftPadded;
        this.fPadded = fPadded;
    }


    public void encodeValue(String data, ByteBuffer byteBuffer) {

        int dataLength = 0;
        dataLength = data.length();
        byte[] b = new byte[getPackedLength(dataLength)];
        CodecUtil.str2hex((String) data, leftPadded, b, 0);

        int paddedSize = dataLength >> 1;
        if (fPadded && dataLength % 2 == 1)
            if (leftPadded)
                b[0] |= (byte) 0xF0;
            else
                b[0 + paddedSize] |= (byte) 0x0F;
        byteBuffer.put(b);


    }

    public String decodeValue(ByteBuffer byteBuffer, int length) {
        byte[] b = new byte[getPackedLength(length)];
        byteBuffer.get(b);
        return (CodecUtil.hex2str(b, 0, length, leftPadded));
    }

    public int getPackedLength(int nDataUnits) {
        return (nDataUnits + 1) / 2;
    }
}

