package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.impl.base.Prefixer;

import java.nio.ByteBuffer;

/**
 * The class Bcd prefixer.
 */
public class BCDPrefixer implements Prefixer {
    /**
     * The constant L.
     */
    public static final BCDPrefixer L = new BCDPrefixer(1);
    /**
     * The constant LL.
     */
    public static final BCDPrefixer LL = new BCDPrefixer(2);
    /**
     * The constant LLL.
     */
    public static final BCDPrefixer LLL = new BCDPrefixer(3);
    /**
     * The constant LLLL.
     */
    public static final BCDPrefixer LLLL = new BCDPrefixer(4);
    private int nDigits;

    /**
     * Instantiates a new Bcd prefixer.
     *
     * @param nDigits the n digits
     */
    public BCDPrefixer(int nDigits) {
        this.nDigits = nDigits;
    }

    @Override
    public int encodeLength(int length, ByteBuffer byteBuffer) {
        int packetLength = getPackedLength();
        int pos = byteBuffer.position();
        for (int i = packetLength - 1; i >= 0; i--) {
            int twoDigits = length % 100;
            length /= 100;
            byteBuffer.put(pos + i, (byte) ((twoDigits / 10 << 4) + twoDigits % 10));
        }
        byteBuffer.position(pos + packetLength);
        return packetLength;
    }

    @Override
    public int decodeLength(ByteBuffer byteBuffer) {
        int len = 0;
        for (int i = 0; i < (nDigits + 1) / 2; i++) {
            byte getByte = byteBuffer.get();
            len = 100 * len + ((getByte & 0xF0) >> 4) * 10 + (getByte & 0x0F);
        }
        return len;
    }

    @Override
    public int getPackedLength() {
        return nDigits + 1 >> 1;
    }
}
