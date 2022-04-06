package io.avreen.iso8583.mapper.impl;

import io.avreen.iso8583.common.ISOBitMap;
import io.avreen.iso8583.util.ISOUtil;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * The class Ifb bitmap.
 */
public class IFB_BITMAP extends ISOBitMapMapper {

    /**
     * Instantiates a new Ifb bitmap.
     *
     * @param len         the len
     * @param description the description
     */
    public IFB_BITMAP(int len, String description) {
        super(len, description);
    }


    @Override
    public void write(ISOBitMap c, ByteBuffer byteBuffer) {
        BitSet b = c.getValue();
        int len =
                getLength() >= 8 ?
                        b.length() + 62 >> 6 << 3 : getLength();
        byte[] ab = ISOUtil.bitSet2byte(b, len);
        byteBuffer.put(ab);

    }

    @Override
    public ISOBitMap read(ByteBuffer byteBuffer) {
        int len;
        BitSet bmap = ISOUtil.byte2BitSet(byteBuffer, getLength() << 3);
        ISOBitMap c = createComponent();
        c.setValue(bmap);
        len = bmap.get(1) ? 128 : 64;
        if (getLength() > 16 && bmap.get(1) && bmap.get(65))
            len = 192;
        int lenUnpack = Math.min(getLength(), len >> 3);
        byteBuffer.position(byteBuffer.position() + lenUnpack);
        return c;

    }
}
