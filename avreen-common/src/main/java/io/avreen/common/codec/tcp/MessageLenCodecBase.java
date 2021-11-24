package io.avreen.common.codec.tcp;

/**
 * The class Message len codec base.
 */
public abstract class MessageLenCodecBase implements IMessageLenCodec {

    private int lengthBytes = 4;
    private int maxLenValue = 9999;

    /**
     * Instantiates a new Message len codec base.
     */
    public MessageLenCodecBase() {

    }

    /**
     * Instantiates a new Message len codec base.
     *
     * @param lengthBytes the length bytes
     */
    public MessageLenCodecBase(int lengthBytes) {
        this.lengthBytes = lengthBytes;
    }

    /**
     * Instantiates a new Message len codec base.
     *
     * @param lengthBytes the length bytes
     * @param maxLenValue the max len value
     */
    public MessageLenCodecBase(int lengthBytes, int maxLenValue) {
        this.lengthBytes = lengthBytes;
        this.maxLenValue = maxLenValue;
    }

    /**
     * Gets max len value.
     *
     * @return the max len value
     */
    public int getMaxLenValue() {
        return maxLenValue;
    }

    /**
     * Sets max len value.
     *
     * @param maxLenValue the max len value
     */
    public void setMaxLenValue(int maxLenValue) {
        this.maxLenValue = maxLenValue;
    }


    /**
     * Check length.
     *
     * @param len the len
     */
    protected void checkLength(int len) {
        if (len > maxLenValue)
            throw new RuntimeException("len exceeded (" + len + " > " + maxLenValue + ")");
        else if (len < 0)
            throw new RuntimeException("invalid negative length (" + len + ")");

    }

    @Override
    public int getLengthBytes() {
        return lengthBytes;
    }

    /**
     * Sets length bytes.
     *
     * @param lengthDigits the length digits
     */
    public void setLengthBytes(int lengthDigits) {
        this.lengthBytes = lengthDigits;
    }


    @Override
    public String toString() {
        return "!BaseCodec!";
    }
}

