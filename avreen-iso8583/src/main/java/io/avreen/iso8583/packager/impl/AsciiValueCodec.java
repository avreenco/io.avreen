package io.avreen.iso8583.packager.impl;


import io.avreen.common.util.CharsetSetting;
import io.avreen.iso8583.packager.impl.base.StringValueCodec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * The class Ascii interpreter.
 */
public class AsciiValueCodec implements StringValueCodec {
    /**
     * The constant INSTANCE.
     */
    public static final AsciiValueCodec INSTANCE = new AsciiValueCodec();
    private Charset charset = null;

    /**
     * Instantiates a new Ascii interpreter.
     */
    public AsciiValueCodec() {

    }

    /**
     * Instantiates a new Ascii interpreter.
     *
     * @param charset the charset
     */
    public AsciiValueCodec(Charset charset) {
        this.charset = charset;
    }


    @Override
    public void encodeValue(String data, ByteBuffer byteBuffer) {

        byte[] stringBytes = (data).getBytes(charset == null ? CharsetSetting.DEFAULT : charset);
        byteBuffer.put(stringBytes);


    }

    /**
     * Gets charset.
     *
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

    @Override
    public String decodeValue(ByteBuffer byteBuffer, int length) {
        try {
            byte[] b = new byte[length];
            byteBuffer.get(b);
            return new String(b, charset == null ? CharsetSetting.DEFAULT : charset);

        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException(
                    String.format("Required %d but just got %d bytes", length, 0)
            );
        }
    }


    public int getPackedLength(int nDataUnits) {
        return nDataUnits;
    }
}
