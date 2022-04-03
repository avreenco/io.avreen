package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.impl.base.IValueLengthCodec;

import java.nio.ByteBuffer;

/**
 * The class ascii value length codec
 */
public class AsciiValueLengthCodec implements IValueLengthCodec {
    /**
     * The constant L.
     */
    public static final AsciiValueLengthCodec L = new AsciiValueLengthCodec(1);
    /**
     * The constant LL.
     */
    public static final AsciiValueLengthCodec LL = new AsciiValueLengthCodec(2);
    /**
     * The constant LLL.
     */
    public static final AsciiValueLengthCodec LLL = new AsciiValueLengthCodec(3);
    /**
     * The constant LLLL.
     */
    public static final AsciiValueLengthCodec LLLL = new AsciiValueLengthCodec(4);
    private int nDigits;

    /**
     * Instantiates a new Ascii ValueLengthCodec.
     *
     * @param nDigits the n digits
     */
    private AsciiValueLengthCodec(int nDigits) {
        this.nDigits = nDigits;
    }
    public static AsciiValueLengthCodec of(int nDigits)
    {
        if(nDigits == 1)
            return AsciiValueLengthCodec.L;
        if(nDigits == 2)
            return AsciiValueLengthCodec.LL;
        if(nDigits == 3)
            return AsciiValueLengthCodec.LLL;
        if(nDigits == 4)
            return AsciiValueLengthCodec.LLLL;
        return new AsciiValueLengthCodec(nDigits);
    }

    @Override
    public void encodeLength(int length, ByteBuffer byteBuffer) {
        int n = length;
        // Write the string backwards - I don't know why I didn't see this at first.
        byte[] b = new byte[nDigits];

        for (int i = nDigits - 1; i >= 0; i--) {
            b[i] = ((byte) (n % 10 + '0'));
            n /= 10;
        }
        if (n != 0) {
            throw new RuntimeException("invalid len " + length + ". digits = " + nDigits);
        }
        byteBuffer.put(b);
        return ;
    }

    @Override
    public int decodeLength(ByteBuffer b) {
        int len = 0;
        for (int i = 0; i < nDigits; i++) {
            len = len * 10 + b.get() - (byte) '0';
        }
        return len;
    }


    public int getPackedLength() {
        return nDigits;
    }
}
