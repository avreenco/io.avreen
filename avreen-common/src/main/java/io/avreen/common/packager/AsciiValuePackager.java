package io.avreen.common.packager;


import io.avreen.common.util.CharsetSetting;

import java.nio.ByteBuffer;

/**
 * The class Ascii value packager.
 */
public class AsciiValuePackager implements IValuePackager {
    /**
     * The constant INSTANCE.
     */
    public static final AsciiValuePackager INSTANCE = new AsciiValuePackager();

    private AsciiValuePackager() {
    }

    @Override
    public byte[] pack(Object value) {
        if (value == null)
            return new byte[0];
        String data = value.toString();
        byte[] b = new byte[data.length()];
        try {
            System.arraycopy(data.getBytes(CharsetSetting.DEFAULT), 0, b, 0, data.length());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    @Override
    public Object unpack(ByteBuffer byteBuffer, int length) {
        try {
            byte[] data = new byte[length];
            byteBuffer.get(data);
            return new String(data, CharsetSetting.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
