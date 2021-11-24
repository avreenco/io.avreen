package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.impl.base.Prefixer;

import java.nio.ByteBuffer;

/**
 * The class Ascii prefixer.
 */
public class AsciiPrefixer implements Prefixer {
    /**
     * The constant L.
     */
    public static final AsciiPrefixer L = new AsciiPrefixer(1);
    /**
     * The constant LL.
     */
    public static final AsciiPrefixer LL = new AsciiPrefixer(2);
    /**
     * The constant LLL.
     */
    public static final AsciiPrefixer LLL = new AsciiPrefixer(3);
    /**
     * The constant LLLL.
     */
    public static final AsciiPrefixer LLLL = new AsciiPrefixer(4);
    private int nDigits;

    /**
     * Instantiates a new Ascii prefixer.
     *
     * @param nDigits the n digits
     */
    public AsciiPrefixer(int nDigits) {
        this.nDigits = nDigits;
    }

    @Override
    public int encodeLength(int length, ByteBuffer byteBuffer) {
        int n = length;
        // Write the string backwards - I don't know why I didn't see this at first.
        byte[] b = new byte[nDigits];

        for (int i = nDigits - 1; i >= 0; i--) {
            b[i] = ((byte) (n % 10 + '0'));
            n /= 10;
        }
        if (n != 0) {
            throw new RuntimeException("invalid len " + length + ". Prefixing digits = " + nDigits);
        }
        byteBuffer.put(b);
        return nDigits;
    }

    @Override
    public int decodeLength(ByteBuffer b) {
        int len = 0;
        for (int i = 0; i < nDigits; i++) {
            len = len * 10 + b.get() - (byte) '0';
        }
        return len;
    }

    @Override
    public int getPackedLength() {
        return nDigits;
    }
}
