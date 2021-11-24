package io.avreen.common.codec.tcp;


import io.avreen.common.util.CodecUtil;

/**
 * The class Hex message len codec.
 */
public class HEXMessageLenCodec extends MessageLenCodecBase {

    /**
     * Instantiates a new Hex message len codec.
     */
    public HEXMessageLenCodec() {
        setLengthBytes(2);
    }

    /**
     * Instantiates a new Hex message len codec.
     *
     * @param lengthBytes the length bytes
     */
    public HEXMessageLenCodec(int lengthBytes) {
        super(lengthBytes);
    }

    /**
     * Instantiates a new Hex message len codec.
     *
     * @param lengthBytes the length bytes
     * @param maxLenValue the max len value
     */
    public HEXMessageLenCodec(int lengthBytes, int maxLenValue) {
        super(lengthBytes, maxLenValue);
    }

    @Override
    public byte[] encodeMessageLength(int len) {
        checkLength(len);
        String hexString = Integer.toString(len, 16);
        return CodecUtil.hex2byte(CodecUtil.zeropad(hexString, getLengthBytes() * 2));
    }

    @Override
    public int decodeMessageLength(byte[] b) {
        int l;
        try {
            l = Integer.parseInt(CodecUtil.hexString(b), 16);
            checkLength(l);

        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid message length " + CodecUtil.hexString(b));
        }
        return l;
    }

    @Override
    public String toString() {
        return "HEXCodec";
    }
}

