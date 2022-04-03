package io.avreen.iso8583.packager.impl.base;

import java.nio.ByteBuffer;

/**
 * The interface Interpreter.
 *
 * @param <T> the type parameter
 */
public interface IValueCodec<T> {

    /**
     * Interpret int.
     *
     * @param data the data
     * @param b    the b
     */
    void encodeValue(T data, ByteBuffer b);

    /**
     * Uninterpret t.
     *
     * @param b      the b
     * @param length the length
     * @return the t
     */
    T decodeValue(ByteBuffer b, int length);


}
