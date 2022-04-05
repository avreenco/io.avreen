package io.avreen.iso8583.mapper.impl;

import io.avreen.iso8583.common.ISOBitMap;
import io.avreen.iso8583.mapper.impl.base.ISOComponentMapperBase;

import java.util.BitSet;

/**
 * The class Iso bit map mapper.
 */
public abstract class ISOBitMapMapper extends ISOComponentMapperBase<BitSet, ISOBitMap> {

    /**
     * Instantiates a new Iso bit map mapper.
     *
     * @param len         the len
     * @param description the description
     */
    public ISOBitMapMapper(int len, String description) {
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
