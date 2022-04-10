package io.avreen.redis.common;

import io.avreen.serializer.ObjectSerializer;
import io.lettuce.core.codec.RedisCodec;

import java.nio.ByteBuffer;

/**
 * The class String object codec.
 */
public class StringObjectCodec implements RedisCodec<String, Object> {
    /**
     * The constant INSTANCE.
     */
    public static final StringObjectCodec INSTANCE = new StringObjectCodec();
    private static final byte[] EMPTY = new byte[0];

    private StringObjectCodec() {
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

    public String decodeKey(ByteBuffer bytes) {
        return getString(bytes);
    }

    public Object decodeValue(ByteBuffer bytes) {
        return ObjectSerializer.current().readClassAndObject(getBytes(bytes));
    }

    public ByteBuffer encodeKey(String key) {
        return key == null ? ByteBuffer.wrap(EMPTY) : ByteBuffer.wrap(key.getBytes());
    }

    public ByteBuffer encodeValue(Object value) {
        return value == null ? ByteBuffer.wrap(EMPTY) : ByteBuffer.wrap(ObjectSerializer.current().writeClassAndObject(value));
    }

}
