package io.avreen.iso8583.packager.impl;

import io.avreen.iso8583.common.ISOBitMap;
import io.avreen.iso8583.util.ISOUtil;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * The class Ifb bitmap.
 */
public class IFB_BITMAP extends ISOBitMapPackager {

    /**
     * Instantiates a new Ifb bitmap.
     *
     * @param len         the len
     * @param description the description
     */
    public IFB_BITMAP(int len, String description) {
        super(len, description);
    }

    /**
     * Gets max packed length.
     *
     * @return the max packed length
     */
    public int getMaxPackedLength() {
        return getLength() >> 3;
    }

    @Override
    public int pack(ISOBitMap c, ByteBuffer byteBuffer) {
        BitSet b = c.getValue();
        int len =                                           // bytes needed to encode BitSet (in 8-byte chunks)
                getLength() >= 8 ?
                        b.length() + 62 >> 6 << 3 : getLength();    // +62 because we don't use bit 0 in the BitSet
        byte[] ab = ISOUtil.bitSet2byte(b, len);
        byteBuffer.put(ab);
        return ab.length;

    }

    @Override
    public int unpack(ISOBitMap c, ByteBuffer byteBuffer) {
        int len;
        BitSet bmap = ISOUtil.byte2BitSet(byteBuffer, getLength() << 3);
        c.setValue(bmap);
        len = bmap.get(1) ? 128 : 64;
        if (getLength() > 16 && bmap.get(1) && bmap.get(65))
            len = 192;
        int lenUnpack = Math.min(getLength(), len >> 3);
        byteBuffer.position(byteBuffer.position() + lenUnpack);
        return lenUnpack;

    }
}
