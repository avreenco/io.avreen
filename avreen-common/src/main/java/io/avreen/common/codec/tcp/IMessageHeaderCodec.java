package io.avreen.common.codec.tcp;

import io.avreen.common.context.MessageTypes;

/**
 * The interface Message header codec.
 */
public interface IMessageHeaderCodec {
    /**
     * Gets header length.
     *
     * @return the header length
     */
    int getHeaderLength();

    /**
     * Encode header byte [ ].
     *
     * @param header      the header
     * @param messageType the message type
     * @param rejectCode  the reject code
     * @return the byte [ ]
     */
    byte[] encodeHeader(byte[] header, MessageTypes messageType, Integer rejectCode);

}
