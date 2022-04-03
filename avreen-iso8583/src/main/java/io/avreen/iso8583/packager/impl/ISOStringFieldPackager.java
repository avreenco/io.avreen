package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.common.ISOStringField;
import io.avreen.iso8583.packager.impl.base.ISOComponentPackagerBase;
import io.avreen.iso8583.packager.impl.base.IValueCodec;
import io.avreen.iso8583.packager.impl.base.Padder;
import io.avreen.iso8583.packager.impl.base.IValueLengthCodec;

/**
 * The class Iso string field packager.
 */
public class ISOStringFieldPackager extends ISOComponentPackagerBase<String, ISOStringField> {
    /**
     * Instantiates a new Iso string field packager.
     *
     * @param maxLength   the max length
     * @param description the description
     * @param padder      the padder
     * @param valueCodec the interpreter
     * @param valueLengthCodec    the valueLengthCodec
     */
    public ISOStringFieldPackager(int maxLength, String description, Padder<String> padder,
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
