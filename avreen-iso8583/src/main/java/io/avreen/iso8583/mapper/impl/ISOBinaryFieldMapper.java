package io.avreen.iso8583.mapper.impl;

import io.avreen.iso8583.common.ISOBinaryField;
import io.avreen.iso8583.mapper.impl.base.ISOComponentMapperBase;
import io.avreen.iso8583.mapper.impl.base.IValueCodec;
import io.avreen.iso8583.mapper.impl.base.Padder;
import io.avreen.iso8583.mapper.impl.base.IValueLengthCodec;

/**
 * The class Iso binary field mapper.
 */
public class ISOBinaryFieldMapper extends ISOComponentMapperBase<byte[], ISOBinaryField> {
    /**
     * Instantiates a new Iso binary field mapper.
     *
     * @param maxLength   the max length
     * @param description the description
     * @param padder      the padder
     * @param interpreter the interpreter
     * @param valueLengthCodec    the valueLengthCodec
     */
    public ISOBinaryFieldMapper(int maxLength, String description, Padder<byte[]> padder,
                                IValueCodec<byte[]> interpreter, IValueLengthCodec valueLengthCodec) {
        super(maxLength, description);
        setPadder(padder);
        setValueCodec(interpreter);
        setValueLengthCodec(valueLengthCodec);
    }

    /**
     * Instantiates a new Iso binary field mapper.
     *
     * @param maxLength   the max length
     * @param description the description
     * @param interpreter the interpreter
     * @param valueLengthCodec    the valueLengthCodec
     */
    public ISOBinaryFieldMapper(int maxLength, String description,
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
