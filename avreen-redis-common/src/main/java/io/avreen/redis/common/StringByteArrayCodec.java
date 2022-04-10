package io.avreen.redis.common;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.ToByteBufEncoder;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * The class String byte array codec.
 */
public class StringByteArrayCodec implements RedisCodec<String, byte[]>, ToByteBufEncoder<String, byte[]> {
    /**
     * The constant INSTANCE.
     */
    public static final StringByteArrayCodec INSTANCE = new StringByteArrayCodec();
    private static final byte[] EMPTY = new byte[0];

    private StringByteArrayCodec() {
    }

    private static byte[] getBytes(ByteBuffer buffer) {
        int remaining = buffer.remaining();
        if (remaining == 0) {
            return EMPTY;
        } else {
            byte[] b = new byte[remaining];
            buffer.get(b);
            return b;
        }
    }

    private static String getString(ByteBuffer buffer) {
        int remaining = buffer.remaining();
        if (remaining == 0) {
            return "";
        } else {
            byte[] b = new byte[remaining];
            buffer.get(b);
            return new String(b);
        }
    }

    public void encodeKey(String key, ByteBuf target) {
        if (key != null) {
            target.writeBytes(key.getBytes());
        }

    }

    private void encodeBytesArray(byte[] value, ByteBuf target) {
        if (value != null) {
            target.writeBytes(value);
        }

    }

    public void encodeValue(byte[] value, ByteBuf target) {
        this.encodeBytesArray(value, target);
    }

    public int estimateSize(Object keyOrValue) {
        if (keyOrValue == null)
            return 0;
        if (keyOrValue instanceof String) {
            return ((String) keyOrValue).length();
        }
        if (keyOrValue instanceof byte[]) {
            return ((byte[]) keyOrValue).length;
        }
        return 0;
    }

    public String decodeKey(ByteBuffer bytes) {
        return getString(bytes);
    }

    public byte[] decodeValue(ByteBuffer bytes) {
        return getBytes(bytes);
    }

    public ByteBuffer encodeKey(String key) {
        return key == null ? ByteBuffer.wrap(EMPTY) : ByteBuffer.wrap(key.getBytes());
    }

    public ByteBuffer encodeValue(byte[] value) {
        return value == null ? ByteBuffer.wrap(EMPTY) : ByteBuffer.wrap(value);
    }

}
