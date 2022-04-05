
package io.avreen.iso8583.mapper.impl;

import io.avreen.common.util.CodecUtil;
import io.avreen.iso8583.common.ISOBitMap;
import io.avreen.iso8583.util.ISOUtil;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * The class Ifa bitmap.
 */
public class IFA_BITMAP extends ISOBitMapMapper {
    /**
     * Instantiates a new Ifa bitmap.
     *
     * @param len         the len
     * @param description the description
     */
    public IFA_BITMAP(int len, String description) {
        super(len, description);
    }


    @Override
    public void write(ISOBitMap c, ByteBuffer bytebuffer) {
        BitSet b = c.getValue();
        int len =
                getLength() >= 8 ?
                        b.length() + 62 >> 6 << 3 : getLength();
        byte[] ab = CodecUtil.hexString(ISOUtil.bitSet2byte(b, len)).getBytes();
        bytebuffer.put(ab);

    }

    @Override
    public ISOBitMap read(ByteBuffer byteBuffer) {
        int len;
        BitSet bmap = ISOUtil.hex2BitSet(byteBuffer, getLength() << 3);
        ISOBitMap c = createComponent();
        c.setValue(bmap);
        len = bmap.get(1) ? 128 : 64; /* changed by Hani */
        if (getLength() > 16 && bmap.get(65)) {
            len = 192;
            bmap.clear(65);
        }
        int lenUnpack = Math.min(getLength() << 1, len >> 2);
        byteBuffer.position(byteBuffer.position() + lenUnpack);
        return c;

    }


}
