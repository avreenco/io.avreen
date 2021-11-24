package io.avreen.common.codec.tcp;

/**
 * The interface Message len codec.
 */
public interface IMessageLenCodec {

    /**
     * Encode message length byte [ ].
     *
     * @param len the len
     * @return the byte [ ]
     */
    byte[] encodeMessageLength(int len);

    /**
     * Decode message length int.
     *
     * @param bytes the bytes
     * @return the int
     */
    int decodeMessageLength(byte[] bytes);

    /**
     * Gets length bytes.
     *
     * @return the length bytes
     */
    int getLengthBytes();
}
