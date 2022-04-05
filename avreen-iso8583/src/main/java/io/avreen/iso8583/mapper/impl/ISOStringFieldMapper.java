package io.avreen.iso8583.mapper.impl;

import io.avreen.iso8583.common.ISOStringField;
import io.avreen.iso8583.mapper.impl.base.ISOComponentMapperBase;
import io.avreen.iso8583.mapper.impl.base.IValueCodec;
import io.avreen.iso8583.mapper.impl.base.Padder;
import io.avreen.iso8583.mapper.impl.base.IValueLengthCodec;

/**
 * The class Iso string field mapper.
 */
public class ISOStringFieldMapper extends ISOComponentMapperBase<String, ISOStringField> {
    /**
     * Instantiates a new Iso string field mapper.
     *
     * @param maxLength   the max length
     * @param description the description
     * @param padder      the padder
     * @param valueCodec the interpreter
     * @param valueLengthCodec    the valueLengthCodec
     */
    public ISOStringFieldMapper(int maxLength, String description, Padder<String> padder,
                                IValueCodec<String> valueCodec, IValueLengthCodec valueLengthCodec) {
        super(maxLength, description);
        setPadder(padder);
        setValueCodec(valueCodec);
        setValueLengthCodec(valueLengthCodec);
    }

    public ISOStringField createComponent() {
        return new ISOStringField();
    }

    @Override
    protected int getValueLength(String value) {
        return value.length();
    }

}
