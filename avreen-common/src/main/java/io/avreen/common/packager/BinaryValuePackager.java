package io.avreen.common.packager;

import java.nio.ByteBuffer;

/**
 * The class Binary value packager.
 */
public class BinaryValuePackager implements IValuePackager {

    /**
     * The constant INSTANCE.
     */
    public static final BinaryValuePackager INSTANCE = new BinaryValuePackager();

    private BinaryValuePackager() {

    }

    @Override
    public byte[] pack(Object value) {
        if (value == null)
            return new byte[0];
        return ((byte[]) value);
    }

    @Override
    public Object unpack(ByteBuffer byteBuffer, int length) {
        byte[] bb = new byte[length];
        byteBuffer.get(bb);
        return bb;
    }
}
