package io.avreen.iso8583.mapper.impl;


import io.avreen.iso8583.common.ISOStringField;
import io.avreen.iso8583.mapper.impl.base.ISOComponentMapperBase;

import java.nio.ByteBuffer;

/**
 * The class If nop.
 */
public class IF_EMPTY extends ISOComponentMapperBase<String, ISOStringField> {
    public IF_EMPTY() {
        super(0, "<dummy>");
    }

    public IF_EMPTY(String description) {
        super(0, description);
    }

    @Override
    protected int getValueLength(String value) {
        return 0;
    }

    @Override
    public ISOStringField createComponent() {
        return null;
    }

    @Override
    public void write(ISOStringField c, ByteBuffer b) {
        return;
    }

    @Override
    public ISOStringField read(ByteBuffer b) {
        return new ISOStringField();
    }

}
