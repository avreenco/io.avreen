
package io.avreen.iso8583.mapper.impl;


import io.avreen.common.util.CodecUtil;
import io.avreen.iso8583.common.ISOBitMap;
import io.avreen.iso8583.util.ISOUtil;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * The class If hex bitmap.
 */
public class IFHex_BITMAP extends ISOBitMapMapper {
    /**
     * Instantiates a new If hex bitmap.
     *
     * @param len         the len
     * @param description the description
     */
    public IFHex_BITMAP(int len, String description) {
        super(len, description);
    }



    @Override
    public void write(ISOBitMap c, ByteBuffer byteBuffer) {
        BitSet bmap = c.getValue();
        int len = getLength();
        byte[] b = ISOUtil.bitSet2byte(bmap, len);
        byte[] allBytes = CodecUtil.hexString(b).getBytes();
        byteBuffer.put(allBytes);
    }

    @Override
    public ISOBitMap read(ByteBuffer byteBuffer) {
        int len = getLength();
        byte[] bytes = new byte[len * 2];
        byteBuffer.get(bytes);
        byte[] hexBytes = CodecUtil.hex2byte(new String(bytes));
        int maxBits = len << 3;
        BitSet bmap = ISOUtil.byte2BitSet(ByteBuffer.wrap(hexBytes), maxBits);
        ISOBitMap c = createComponent();
        c.setValue(bmap);
        return c;
    }

}
