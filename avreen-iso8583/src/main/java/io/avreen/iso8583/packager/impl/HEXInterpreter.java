package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.impl.base.CharacterBaseInterpreter;
import io.avreen.iso8583.util.ISOUtil;

import java.nio.ByteBuffer;

/**
 * The class Hex interpreter.
 */
public class HEXInterpreter implements CharacterBaseInterpreter {
    /**
     * The constant LEFT_PADDED.
     */
    public static final HEXInterpreter LEFT_PADDED = new HEXInterpreter(true, false);
    /**
     * The constant RIGHT_PADDED.
     */
    public static final HEXInterpreter RIGHT_PADDED = new HEXInterpreter(false, false);
    /**
     * The constant RIGHT_PADDED_F.
     */
    public static final HEXInterpreter RIGHT_PADDED_F = new HEXInterpreter(false, true);
    /**
     * The constant LEFT_PADDED_F.
     */
    public static final HEXInterpreter LEFT_PADDED_F = new HEXInterpreter(true, true);

    private boolean leftPadded;
    private boolean fPadded;

    private HEXInterpreter(boolean leftPadded, boolean fPadded) {
        this.leftPadded = leftPadded;
        this.fPadded = fPadded;
    }


    public int interpret(String data, ByteBuffer byteBuffer) {

        int dataLength = 0;
        dataLength = ((String) data).length();
        byte[] b = new byte[getPackedLength(dataLength)];
        ISOUtil.str2hex((String) data, leftPadded, b, 0);

        // if (fPadded && !leftPadded && data.length()%2 == 1)
        //   b[b.length-1] |= (byte)(b[b.length-1] << 4) == 0 ? 0x0F : 0x00;
        int paddedSize = dataLength >> 1;
        if (fPadded && dataLength % 2 == 1)
            if (leftPadded)
                b[0] |= (byte) 0xF0;
            else
                b[0 + paddedSize] |= (byte) 0x0F;
        byteBuffer.put(b);
        return b.length;


    }

    public String uninterpret(ByteBuffer byteBuffer, int length) {
        byte[] b = new byte[getPackedLength(length)];
        byteBuffer.get(b);
        return (ISOUtil.hex2str(b, 0, length, leftPadded));
    }

    public int getPackedLength(int nDataUnits) {
        return (nDataUnits + 1) / 2;
    }
}

