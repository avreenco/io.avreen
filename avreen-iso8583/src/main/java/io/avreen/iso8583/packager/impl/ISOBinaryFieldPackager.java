package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.common.ISOBinaryField;
import io.avreen.iso8583.packager.impl.base.ISOComponentPackagerBase;
import io.avreen.iso8583.packager.impl.base.IValueCodec;
import io.avreen.iso8583.packager.impl.base.Padder;
import io.avreen.iso8583.packager.impl.base.IValueLengthCodec;

/**
 * The class Iso binary field packager.
 */
public class ISOBinaryFieldPackager extends ISOComponentPackagerBase<byte[], ISOBinaryField> {
    /**
     * Instantiates a new Iso binary field packager.
     *
     * @param maxLength   the max length
     * @param description the description
     * @param padder      the padder
     * @param interpreter the interpreter
     * @param valueLengthCodec    the valueLengthCodec
     */
    public ISOBinaryFieldPackager(int maxLength, String description, Padder<byte[]> padder,
                                  IValueCodec<byte[]> interpreter, IValueLengthCodec valueLengthCodec) {
        super(maxLength, description);
        setPadder(padder);
        setValueCodec(interpreter);
        setValueLengthCodec(valueLengthCodec);
    }

    /**
     * Instantiates a new Iso binary field packager.
     *
     * @param maxLength   the max length
     * @param description the description
     * @param interpreter the interpreter
     * @param valueLengthCodec    the valueLengthCodec
     */
    public ISOBinaryFieldPackager(int maxLength, String description,
                                  IValueCodec<byte[]> interpreter, IValueLengthCodec valueLengthCodec) {
        super(maxLength, description);
        setValueCodec(interpreter);
        setValueLengthCodec(valueLengthCodec);
    }

    public ISOBinaryField createComponent() {
        return new ISOBinaryField();
    }

    @Override
    protected int getValueLength(byte[] value) {
        return value.length;
    }
}
