package io.avreen.common.codec.tcp;

import io.avreen.common.util.CodecUtil;

/**
 * The class Ascii message len codec.
 */
public class ASCIIMessageLenCodec extends MessageLenCodecBase {


    /**
     * Instantiates a new Ascii message len codec.
     */
    public ASCIIMessageLenCodec() {

    }

    /**
     * Instantiates a new Ascii message len codec.
     *
     * @param lengthBytes the length bytes
     */
    public ASCIIMessageLenCodec(int lengthBytes) {
        super(lengthBytes);
    }

    /**
     * Instantiates a new Ascii message len codec.
     *
     * @param lengthBytes the length bytes
     * @param maxLenValue the max len value
     */
    public ASCIIMessageLenCodec(int lengthBytes, int maxLenValue) {
        super(lengthBytes, maxLenValue);
    }

    @Override
    public byte[] encodeMessageLength(int len) {
        checkLength(len);
        return CodecUtil.zeropad(len, getLengthBytes()).getBytes();
    }


    @Override
    public int decodeMessageLength(byte[] b) {
        int l = 0;
        try {
            l = Integer.parseInt(new String(b));
            checkLength(l);

        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid message length " + CodecUtil.hexString(b));
        }
        return l;
    }

    @Override
    public String toString() {
        return "ASCIICodec";
    }

}

