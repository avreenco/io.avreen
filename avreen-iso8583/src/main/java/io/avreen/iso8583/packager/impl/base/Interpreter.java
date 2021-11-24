package io.avreen.iso8583.packager.impl.base;

import java.nio.ByteBuffer;

/**
 * The interface Interpreter.
 *
 * @param <T> the type parameter
 */
public interface Interpreter<T> {

    /**
     * Interpret int.
     *
     * @param data the data
     * @param b    the b
     * @return the int
     */
    int interpret(T data, ByteBuffer b);

    /**
     * Uninterpret t.
     *
     * @param b      the b
     * @param length the length
     * @return the t
     */
    T uninterpret(ByteBuffer b, int length);

    /**
     * Gets packed length.
     *
     * @param nBytes the n bytes
     * @return the packed length
     */
    int getPackedLength(int nBytes);

}
