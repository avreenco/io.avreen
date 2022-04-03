package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.impl.base.ByteArrayValueCodec;

import java.nio.ByteBuffer;

/**
 * The class Ascii hex interpreter.
 */
public class AsciiHexValueCodec implements ByteArrayValueCodec {
    /**
     * The constant INSTANCE.
     */
    public static final AsciiHexValueCodec INSTANCE = new AsciiHexValueCodec();

    private static final byte[] HEX_ASCII = new byte[]{
            0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
            0x38, 0x39, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46
    };

    @Override
    public void encodeValue(byte[] data, ByteBuffer b) {
        for (int i = 0; i < data.length; i++) {
            b.put(HEX_ASCII[(data[i] & 0xF0) >> 4]);
            b.put(HEX_ASCII[data[i] & 0x0F]);
        }
    }

    @Override
    public byte[] decodeValue(ByteBuffer byteBuffer, int length) {
        byte[] d = new byte[length];
        for (int i = 0; i < length * 2; i++) {
            int shift = i % 2 == 1 ? 0 : 4;
            d[i >> 1] |= Character.digit((char) byteBuffer.get(), 16) << shift;
        }
        return d;

    }

    public int getPackedLength(int nBytes) {
        return nBytes * 2;
    }
}
