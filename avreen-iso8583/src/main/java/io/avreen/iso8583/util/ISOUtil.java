package io.avreen.iso8583.util;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * The class Iso util.
 */
public class ISOUtil {

    /**
     * Bit set 2 byte byte [ ].
     *
     * @param b     the b
     * @param bytes the bytes
     * @return the byte [ ]
     */
    public static byte[] bitSet2byte(BitSet b, int bytes) {
        int len = bytes * 8;

        byte[] d = new byte[bytes];
        for (int i = 0; i < len; i++)
            if (b.get(i + 1))
                d[i >> 3] |= 0x80 >> i % 8;
        if (len > 64)
            d[0] |= 0x80;
        if (len > 128)
            d[8] |= 0x80;
        return d;
    }


    /**
     * Byte 2 bit set bit set.
     *
     * @param byteBuffer the byte buffer
     * @param maxBits    the max bits
     * @return the bit set
     */
    public static BitSet byte2BitSet(ByteBuffer byteBuffer, int maxBits) {
        int offset = byteBuffer.position();
        boolean b1 = (byteBuffer.get(offset) & 0x80) == 0x80;
        boolean b65 = (byteBuffer.limit() > offset + 8) && ((byteBuffer.get(offset + 8) & 0x80) == 0x80);

        int len = (maxBits > 128 && b1 && b65) ? 192 :
                (maxBits > 64 && b1) ? 128 :
                        (maxBits < 64) ? maxBits : 64;

        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++)
            if ((byteBuffer.get(offset + (i >> 3)) & 0x80 >> i % 8) > 0)
                bmap.set(i + 1);
        return bmap;
    }


    /**
     * Hex 2 bit set bit set.
     *
     * @param byteBuffer the byte buffer
     * @param maxBits    the max bits
     * @return the bit set
     */
    public static BitSet hex2BitSet(ByteBuffer byteBuffer, int maxBits) {
        int offset = byteBuffer.position();
        int len = maxBits > 64 ?
                (Character.digit((char) byteBuffer.get(offset), 16) & 0x08) == 8 ? 128 : 64 :
                maxBits;
        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++) {
            int digit = Character.digit((char) byteBuffer.get(offset + (i >> 2)), 16);
            if ((digit & 0x08 >> i % 4) > 0) {
                bmap.set(i + 1);
            }
        }
        return bmap;
    }


}
