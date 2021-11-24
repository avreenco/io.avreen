package io.avreen.iso8583.packager.impl;


import io.avreen.iso8583.common.ISOStringField;
import io.avreen.iso8583.packager.impl.base.ISOComponentPackagerBase;

import java.nio.ByteBuffer;

/**
 * The class If nop.
 */
public class IF_NOP extends ISOComponentPackagerBase<String, ISOStringField> {
    /**
     * Instantiates a new If nop.
     */
    public IF_NOP() {
        super(0, "<dummy>");
    }

    /**
     * Instantiates a new If nop.
     *
     * @param len         the len
     * @param description the description
     */
    public IF_NOP(int len, String description) {
        super(len, description);
    }

    @Override
    protected int getValueLength(String value) {
        return 0;
    }

    /**
     * Gets max packed length.
     *
     * @return the max packed length
     */
    public int getMaxPackedLength() {
        return 0;
    }

    @Override
    public int pack(ISOStringField c, ByteBuffer b) {
        return 0;
    }

    @Override
    public int unpack(ISOStringField c, ByteBuffer b) {
        return 0;
    }

    public ISOStringField createComponent() {
        return new ISOStringField();
    }

}
