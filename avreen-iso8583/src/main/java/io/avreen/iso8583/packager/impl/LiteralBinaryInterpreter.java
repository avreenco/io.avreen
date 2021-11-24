package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.packager.impl.base.ByteArrayInterpreter;

import java.nio.ByteBuffer;

/**
 * The class Literal binary interpreter.
 */
public class LiteralBinaryInterpreter implements ByteArrayInterpreter {
    /**
     * The constant INSTANCE.
     */
    public static final LiteralBinaryInterpreter INSTANCE = new LiteralBinaryInterpreter();

    private LiteralBinaryInterpreter() {
    }

    public int interpret(byte[] data, ByteBuffer byteBuffer) {
        byteBuffer.put(data);
        return data.length;
    }

    public byte[] uninterpret(ByteBuffer byteBuffer, int length) {
        byte[] ret = new byte[length];
        byteBuffer.get(ret);
        return ret;
    }

    public int getPackedLength(int nBytes) {
        // TODO Auto-generated method stub
        return nBytes;
    }
}
