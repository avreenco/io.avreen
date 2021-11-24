package io.avreen.iso8583.common;

import io.avreen.iso8583.util.ISOUtil;

/**
 * The class Iso binary field.
 */
public class ISOBinaryField
        extends ISOField<byte[]> {

    /**
     * The Value.
     */
    protected byte[] value;

    /**
     * Instantiates a new Iso binary field.
     */
    public ISOBinaryField() {
    }

    /**
     * Instantiates a new Iso binary field.
     *
     * @param v the v
     */
    public ISOBinaryField(byte[] v) {
        value = v;
    }

    public byte[] getValue() {

        return value;
    }

    public void setValue(byte[] obj) {
        value = (byte[]) obj;
    }

    @Override
    public String getType() {
        return "binary";
    }

    public String toString() {
        return ISOUtil.hexString(value);
    }
}
