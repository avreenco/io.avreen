package io.avreen.iso8583.common;

import java.util.BitSet;

/**
 * The class Iso bit map.
 */
public class ISOBitMap extends ISOField<BitSet> {
    private long[] value;
    private transient BitSet bitSetValue;

    /**
     * Instantiates a new Iso bit map.
     */
    public ISOBitMap() {

    }

    /**
     * Instantiates a new Iso bit map.
     *
     * @param v the v
     */
    public ISOBitMap(BitSet v) {
        setValue(v);
    }


    public BitSet getValue() {
        if (bitSetValue != null)
            return bitSetValue;
        /* when deserilize not set manualy may be value set from reflection*/
        if (value != null)
            bitSetValue = BitSet.valueOf(value);
        return bitSetValue;
    }

    public void setValue(BitSet obj) {
        if (obj != null) {
            value = obj.toLongArray();
            bitSetValue = obj;
        }
    }

    @Override
    public String getType() {
        return "bitmap";
    }

    @Override
    public String toString() {
        BitSet bitSetValue = getValue();
        if (bitSetValue != null)
            return bitSetValue.toString();
        else
            return "";
    }
}
