package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.impl.base.Prefixer;

import java.nio.ByteBuffer;

/**
 * The class Binary prefixer.
 */
public class BinaryPrefixer implements Prefixer {
    /**
     * The constant B.
     */
    public static final BinaryPrefixer B = new BinaryPrefixer(1);
    /**
     * The constant BB.
     */
    public static final BinaryPrefixer BB = new BinaryPrefixer(2);
    private int nBytes;

    /**
     * Instantiates a new Binary prefixer.
     *
     * @param nBytes the n bytes
     */
    public BinaryPrefixer(int nBytes) {
        this.nBytes = nBytes;
    }


    @Override
    public int encodeLength(int length, ByteBuffer b) {
        for (int i = nBytes - 1; i >= 0; i--) {
            b.put((byte) (length & 0xFF));
            length >>= 8;
        }
        return nBytes;
    }

    @Override
    public int decodeLength(ByteBuffer b) {
        int len = 0;
        for (int i = 0; i < nBytes; i++) {
            len = 256 * len + (b.get() & 0xFF);
        }
        return len;
    }


    @Override
    public int getPackedLength() {
        return nBytes;
    }
}