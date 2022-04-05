package io.avreen.iso8583.mapper.impl.base;


import java.nio.ByteBuffer;

/**
 * The interface IValueLengthCodec.
 */
public interface IValueLengthCodec {
    /**
     * Encode length int.
     *
     * @param length     the length
     * @param byteBuffer the byte buffer
     */
    void encodeLength(int length, ByteBuffer byteBuffer);

    /**
     * Decode length int.
     *
     * @param b the b
     * @return the int
     */
    int decodeLength(ByteBuffer b);

}