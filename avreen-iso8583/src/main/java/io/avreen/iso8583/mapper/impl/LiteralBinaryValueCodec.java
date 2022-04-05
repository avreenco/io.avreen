package io.avreen.iso8583.mapper.impl;

import io.avreen.iso8583.mapper.impl.base.ByteArrayValueCodec;

import java.nio.ByteBuffer;

/**
 * The class Literal binary interpreter.
 */
public class LiteralBinaryValueCodec implements ByteArrayValueCodec {
    /**
     * The constant INSTANCE.
     */
    public static final LiteralBinaryValueCodec INSTANCE = new LiteralBinaryValueCodec();

    private LiteralBinaryValueCodec() {
    }

    public void encodeValue(byte[] data, ByteBuffer byteBuffer) {
        byteBuffer.put(data);
    }

    public byte[] decodeValue(ByteBuffer byteBuffer, int length) {
        byte[] ret = new byte[length];
        byteBuffer.get(ret);
        return ret;
    }

    public int getPackedLength(int nBytes) {
        // TODO Auto-generated method stub
        return nBytes;
    }
}
