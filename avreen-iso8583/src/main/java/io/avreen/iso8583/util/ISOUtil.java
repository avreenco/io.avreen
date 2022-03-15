package io.avreen.iso8583.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * The class Iso util.
 */
public class ISOUtil {

    public static String padLeft(String s, int len, char c) {

        StringBuilder builder = new StringBuilder(s);
        while (builder.length() < len) {
            builder.append(c);
        }
        return builder.toString();
    }
    /**
     * Zeropad string.
     *
     * @param s   the s
     * @param len the len
     * @return the string
     */
    public static String zeroPad(String s, int len) {
        return padLeft(s, len, '0');
    }

    /**
     * Str 2 bcd byte [ ].
     *
     * @param s       the s
     * @param padLeft the pad left
     * @param d       the d
     * @param offset  the offset
     * @return the byte [ ]
     */
    public static byte[] str2bcd(String s, boolean padLeft, byte[] d, int offset) {
        int len = s.length();
        int start = (len & 1) == 1 && padLeft ? 1 : 0;
        for (int i = start; i < len + start; i++)
            d[offset + (i >> 1)] |= s.charAt(i - start) - '0' << ((i & 1) == 1 ? 0 : 4);
        return d;
    }

    /**
     * Str 2 hex byte [ ].
     *
     * @param s       the s
     * @param padLeft the pad left
     * @param d       the d
     * @param offset  the offset
     * @return the byte [ ]
     */
    public static byte[] str2hex(String s, boolean padLeft, byte[] d, int offset) {
        int len = s.length();
        int start = (len & 1) == 1 && padLeft ? 1 : 0;
        for (int i = start; i < len + start; i++)
            d[offset + (i >> 1)] |= Character.digit(s.charAt(i - start), 16) << ((i & 1) == 1 ? 0 : 4);
        return d;
    }


    /**
     * Bcd 2 str string.
     *
     * @param b       the b
     * @param offset  the offset
     * @param len     the len
     * @param padLeft the pad left
     * @return the string
     */
    public static String bcd2str(byte[] b, int offset,
                                 int len, boolean padLeft) {
        StringBuilder d = new StringBuilder(len);
        int start = (len & 1) == 1 && padLeft ? 1 : 0;
        for (int i = start; i < len + start; i++) {
            int shift = (i & 1) == 1 ? 0 : 4;
            char c = Character.forDigit(
                    b[offset + (i >> 1)] >> shift & 0x0F, 16);
            if (c == 'd')
                c = '=';
            d.append(Character.toUpperCase(c));
        }
        return d.toString();
    }


    /**
     * Hex 2 str string.
     *
     * @param b       the b
     * @param offset  the offset
     * @param len     the len
     * @param padLeft the pad left
     * @return the string
     */
    public static String hex2str(byte[] b, int offset,
                                 int len, boolean padLeft) {
        StringBuilder d = new StringBuilder(len);
        int start = (len & 1) == 1 && padLeft ? 1 : 0;

        for (int i = start; i < len + start; i++) {
            int shift = (i & 1) == 1 ? 0 : 4;
            char c = Character.forDigit(
                    b[offset + (i >> 1)] >> shift & 0x0F, 16);
            d.append(Character.toUpperCase(c));
        }
        return d.toString();
    }


    /**
     * Hex string string.
     *
     * @param b the b
     * @return the string
     */
    public static String hexString(byte[] b) {
        return new BigInteger(b).toString(16);
    }


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
            if (b.get(i + 1))                     // +1 because we don't use bit 0 of the BitSet
                d[i >> 3] |= 0x80 >> i % 8;
        //TODO: review why 2nd & 3rd bit map flags are set here??? 
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
        if (len > 64 && maxBits > 128 &&
                byteBuffer.limit() > 16 &&
                (Character.digit((char) byteBuffer.get(offset + 16), 16) & 0x08) == 8) {
            len = 192;
        }
        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++) {
            int digit = Character.digit((char) byteBuffer.get(offset + (i >> 2)), 16);
            if ((digit & 0x08 >> i % 4) > 0) {
                bmap.set(i + 1);
                if (i == 65 && maxBits > 128)     // BBB this is redundant (check already done outside
                    len = 192;                  // BBB of the loop), but I'll leave it for now..
            }
        }
        return bmap;
    }

    /**
     * Hex 2 byte byte [ ].
     *
     * @param s the s
     * @return the byte [ ]
     */
    public static byte[] hex2byte(String s) {
        if (s.length() % 2 == 0) {
            return new BigInteger(s, 16).toByteArray();
        } else {
            return new BigInteger("0" + s, 16).toByteArray();
        }
    }


}
