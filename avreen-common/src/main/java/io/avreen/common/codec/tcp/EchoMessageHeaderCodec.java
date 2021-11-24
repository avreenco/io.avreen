package io.avreen.common.codec.tcp;

import io.avreen.common.context.MessageTypes;

/**
 * The class Echo message header codec.
 */
public class EchoMessageHeaderCodec implements IMessageHeaderCodec {


    private int headerLen;

    /**
     * Instantiates a new Echo message header codec.
     *
     * @param headerLen the header len
     */
    public EchoMessageHeaderCodec(int headerLen) {
        this.headerLen = headerLen;
    }

    /**
     * Instantiates a new Echo message header codec.
     */
    public EchoMessageHeaderCodec() {
    }

    /**
     * Gets header len.
     *
     * @return the header len
     */
    public int getHeaderLen() {
        return headerLen;
    }

    /**
     * Sets header len.
     *
     * @param headerLen the header len
     */
    public void setHeaderLen(int headerLen) {
        this.headerLen = headerLen;
    }

    @Override
    public int getHeaderLength() {
        return headerLen;
    }

    @Override
    public byte[] encodeHeader(byte[] header, MessageTypes messageType, Integer rejectCode) {
        return header;
    }
}
