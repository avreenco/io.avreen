package org.avreen.security.module.api;

/**
 * The interface Byte array converter.
 */
public interface IByteArrayConverter {
    /**
     * Encode byte [ ].
     *
     * @return the byte [ ]
     */
    byte[] encode();

    /**
     * Decode.
     *
     * @param encoded the encoded
     */
    void decode(byte[] encoded);
}
