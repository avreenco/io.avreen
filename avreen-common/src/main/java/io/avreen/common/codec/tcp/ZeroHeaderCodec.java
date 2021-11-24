package io.avreen.common.codec.tcp;

import io.avreen.common.context.MessageTypes;

/**
 * The class Zero header codec.
 */
public class ZeroHeaderCodec implements IMessageHeaderCodec {
    @Override
    public int getHeaderLength() {
        return 0;
    }

    @Override
    public byte[] encodeHeader(byte[] header, MessageTypes messageType, Integer rejectCode) {
        return new byte[0];
    }
}
