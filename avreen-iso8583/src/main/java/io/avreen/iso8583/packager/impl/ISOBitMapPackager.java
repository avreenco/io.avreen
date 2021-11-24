package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.common.ISOBitMap;
import io.avreen.iso8583.packager.impl.base.ISOComponentPackagerBase;

import java.util.BitSet;

/**
 * The class Iso bit map packager.
 */
public abstract class ISOBitMapPackager extends ISOComponentPackagerBase<BitSet, ISOBitMap> {

    /**
     * Instantiates a new Iso bit map packager.
     *
     * @param len         the len
     * @param description the description
     */
    public ISOBitMapPackager(int len, String description) {
        super(len, description);
    }

    public ISOBitMap createComponent() {
        return new ISOBitMap();
    }

    @Override
    protected int getValueLength(BitSet value) {
        return 0;
    }

}
