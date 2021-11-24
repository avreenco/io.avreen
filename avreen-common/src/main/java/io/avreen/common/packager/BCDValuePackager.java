package io.avreen.common.packager;


import io.avreen.common.util.CodecUtil;

import java.nio.ByteBuffer;

/**
 * The class Bcd value packager.
 */
public class BCDValuePackager implements IValuePackager {
    /**
     * The constant RIGHT_PAD_F.
     */
    public static final BCDValuePackager RIGHT_PAD_F = new BCDValuePackager(2);
    /**
     * The constant LEFT_PAD_0.
     */
    public static final BCDValuePackager LEFT_PAD_0 = new BCDValuePackager(1);

    /**
     * The constant LEFT_0.
     */
    public static final int LEFT_0 = 1;
    /**
     * The constant RIGHT_F.
     */
    public static final int RIGHT_F = 2;

    private int mode;

    private BCDValuePackager(int mode) {
        this.mode = mode;
    }

    private int getPackedLength(int nDataUnits) {
        return (nDataUnits + 1) / 2;
    }

    @Override
    public byte[] pack(Object value) {
        if (value == null)
            return new byte[0];

        String data = value.toString();
        int dataLength = 0;

        dataLength = (data).length();
        byte[] b = new byte[getPackedLength(dataLength)];

        boolean leftPadded = mode == LEFT_0;
        boolean fPadded = mode == RIGHT_F;

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

    @Override
    public Object unpack(ByteBuffer byteBuffer, int length) {

        byte[] b = new byte[length];
        byteBuffer.get(b);
        boolean leftPadded = mode == LEFT_0;
        return CodecUtil.bcd2str(b, 0, length, leftPadded);
    }
}
