
package io.avreen.iso8583.packager.impl;


import io.avreen.iso8583.common.ISOBitMap;
import io.avreen.iso8583.util.ISOUtil;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * The class If hex bitmap.
 */
public class IFHex_BITMAP extends ISOBitMapPackager {
    /**
     * Instantiates a new If hex bitmap.
     *
     * @param len         the len
     * @param description the description
     */
    public IFHex_BITMAP(int len, String description) {
        super(len, description);
    }

    /**
     * Gets max packed length.
     *
     * @return the max packed length
     */
    public int getMaxPackedLength() {
        return getLength() >> 2;
    }

    @Override
    public int pack(ISOBitMap c, ByteBuffer byteBuffer) {
        BitSet bmap = c.getValue();
        int len = getLength();
        byte[] b = ISOUtil.bitSet2byte(bmap, len);
        byte[] allBytes = ISOUtil.hexString(b).getBytes();
        byteBuffer.put(allBytes);
        return allBytes.length;
    }

    @Override
    public int unpack(ISOBitMap c, ByteBuffer byteBuffer) {
        int len = getLength();
        byte[] bytes = new byte[len * 2];
        byteBuffer.get(bytes);
        byte[] hexBytes = ISOUtil.hex2byte(new String(bytes));
        int maxBits = len << 3;
        BitSet bmap = ISOUtil.byte2BitSet(ByteBuffer.wrap(hexBytes), maxBits);
        c.setValue(bmap);
        return len * 2;
    }

}
