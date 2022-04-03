package io.avreen.iso8583.packager.impl;

import io.avreen.common.util.CodecUtil;
import io.avreen.iso8583.packager.impl.base.StringValueCodec;

import java.nio.ByteBuffer;

/**
 * The class Bcd interpreter.
 */
public class BCDValueCodec implements StringValueCodec {
    /**
     * The constant LEFT_PADDED.
     */
    public static final BCDValueCodec LEFT_PADDED = new BCDValueCodec(true, false);
    /**
     * The constant RIGHT_PADDED.
     */
    public static final BCDValueCodec RIGHT_PADDED = new BCDValueCodec(false, false);
    /**
     * The constant RIGHT_PADDED_F.
     */
    public static final BCDValueCodec RIGHT_PADDED_F = new BCDValueCodec(false, true);
    /**
     * The constant LEFT_PADDED_F.
     */
    public static final BCDValueCodec LEFT_PADDED_F = new BCDValueCodec(true, true);

    private boolean leftPadded;
    private boolean fPadded;

    private BCDValueCodec(boolean leftPadded, boolean fPadded) {
        this.leftPadded = leftPadded;
        this.fPadded = fPadded;
    }

    /**
     * Interpret byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    public byte[] interpret(String data) {
        int dataLength = 0;

        dataLength = data.length();
        byte[] b = new byte[getPackedLength(dataLength)];

        CodecUtil.str2bcd((String) data, leftPadded, b, 0);
        // if (fPadded && !leftPadded && data.length()%2 == 1)
        //   b[b.length-1] |= (byte)(b[b.length-1] << 4) == 0 ? 0x0F : 0x00;
        int paddedSize = dataLength >> 1;
        if (fPadded && dataLength % 2 == 1)
            if (leftPadded)
                b[0] |= (byte) 0xF0;
            else
                b[0 + paddedSize] |= (byte) 0x0F;
        return b;
    }


    public void encodeValue(String data, ByteBuffer byteBuffer) {
        byte[] b = interpret(data);
        byteBuffer.put(b);
    }

    public String decodeValue(ByteBuffer byteBuffer, int length) {
        byte[] b = new byte[getPackedLength(length)];
        byteBuffer.get(b);
        return uninterpret(b, length);
    }

    /**
     * Uninterpret string.
     *
     * @param b      the b
     * @param length the length
     * @return the string
     */
    public String uninterpret(byte[] b, int length) {
        return CodecUtil.bcd2str(b, 0, length, leftPadded);
    }

    public int getPackedLength(int nDataUnits) {
        return (nDataUnits + 1) / 2;
    }
}
