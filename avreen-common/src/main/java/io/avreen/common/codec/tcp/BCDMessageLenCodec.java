package io.avreen.common.codec.tcp;


import io.avreen.common.util.CodecUtil;

/**
 * The class Bcd message len codec.
 */
public class BCDMessageLenCodec extends MessageLenCodecBase {

    /**
     * Instantiates a new Bcd message len codec.
     */
    public BCDMessageLenCodec() {
        setLengthBytes(2);
    }

    /**
     * Instantiates a new Bcd message len codec.
     *
     * @param lengthBytes the length bytes
     */
    public BCDMessageLenCodec(int lengthBytes) {
        super(lengthBytes);
    }

    /**
     * Instantiates a new Bcd message len codec.
     *
     * @param lengthBytes the length bytes
     * @param maxLenValue the max len value
     */
    public BCDMessageLenCodec(int lengthBytes, int maxLenValue) {
        super(lengthBytes, maxLenValue);
    }

    @Override
    public byte[] encodeMessageLength(int len) {
        checkLength(len);

        return CodecUtil.str2bcd(CodecUtil.zeropad(Integer.toString(len), getLengthBytes() * 2), true);
    }

    @Override
    public int decodeMessageLength(byte[] b) {
        int l;
        try {
            l = Integer.parseInt(CodecUtil.bcd2str(b, 0, getLengthBytes() * 2, true));
            checkLength(l);

        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid message length " + CodecUtil.hexString(b));
        }
        return l;
    }

    @Override
    public String toString() {
        return "BCDCodec";
    }
}

