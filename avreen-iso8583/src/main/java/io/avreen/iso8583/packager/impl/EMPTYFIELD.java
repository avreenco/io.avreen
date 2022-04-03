package io.avreen.iso8583.packager.impl;


import io.avreen.iso8583.common.ISOStringField;
import io.avreen.iso8583.packager.impl.base.ISOComponentPackagerBase;

import java.nio.ByteBuffer;

/**
 * The class If nop.
 */
public class EMPTYFIELD extends ISOComponentPackagerBase<String, ISOStringField> {
    public EMPTYFIELD() {
        super(0, "<dummy>");
    }

    public EMPTYFIELD(String description) {
        super(0, description);
    }

    @Override
    protected int getValueLength(String value) {
        return 0;
    }

    @Override
    public void pack(ISOStringField c, ByteBuffer b) {
        return;
    }

    @Override
    public void unpack(ISOStringField c, ByteBuffer b) {
        return;
    }

    public ISOStringField createComponent() {
        return new ISOStringField();
    }

}
