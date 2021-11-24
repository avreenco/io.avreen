package io.avreen.iso8583.packager.impl.base;


import java.nio.ByteBuffer;

/**
 * The interface Prefixer.
 */
public interface Prefixer {
    /**
     * Encode length int.
     *
     * @param length     the length
     * @param byteBuffer the byte buffer
     * @return the int
     */
    int encodeLength(int length, ByteBuffer byteBuffer);

    /**
     * Decode length int.
     *
     * @param b the b
     * @return the int
     */
    int decodeLength(ByteBuffer b);

    /**
     * Gets packed length.
     *
     * @return the packed length
     */
    int getPackedLength();
}