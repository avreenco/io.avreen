package io.avreen.iso8583.packager.impl;


import io.avreen.common.util.CharsetSetting;
import io.avreen.iso8583.packager.impl.base.CharacterBaseInterpreter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * The class Ascii interpreter.
 */
public class AsciiInterpreter implements CharacterBaseInterpreter {
    /**
     * The constant INSTANCE.
     */
    public static final AsciiInterpreter INSTANCE = new AsciiInterpreter();
    private Charset charset = null;

    /**
     * Instantiates a new Ascii interpreter.
     */
    public AsciiInterpreter() {

    }

    /**
     * Instantiates a new Ascii interpreter.
     *
     * @param charset the charset
     */
    public AsciiInterpreter(Charset charset) {
        this.charset = charset;
    }


    @Override
    public int interpret(String data, ByteBuffer byteBuffer) {

        byte[] stringBytes = (data).getBytes(charset == null ? CharsetSetting.DEFAULT : charset);
        byteBuffer.put(stringBytes);
        return stringBytes.length;


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
    public String uninterpret(ByteBuffer byteBuffer, int length) {
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

    @Override
    public int getPackedLength(int nDataUnits) {
        return nDataUnits;
    }
}
