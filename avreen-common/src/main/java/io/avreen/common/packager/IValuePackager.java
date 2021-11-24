package io.avreen.common.packager;

import java.nio.ByteBuffer;

/**
 * The interface Value packager.
 */
public interface IValuePackager {
    /**
     * Pack byte [ ].
     *
     * @param value the value
     * @return the byte [ ]
     */
    byte[] pack(Object value);

    /**
     * Unpack object.
     *
     * @param byteBuffer the byte buffer
     * @param length     the length
     * @return the object
     */
    Object unpack(ByteBuffer byteBuffer, int length);
}
