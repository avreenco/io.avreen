package io.avreen.iso8583.util;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class Iso util.
 */
public class ISOUtil {
    /**
     * The constant hexStrings.
     */
    public static final String[] hexStrings;
    /**
     * The constant ENCODING.
     */
    public static final String ENCODING = "ISO8859_1";
    /**
     * The constant CHARSET.
     */
    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;
    /**
     * The constant EBCDIC.
     */
    public static final Charset EBCDIC = Charset.forName("IBM1047");
    /**
     * The constant STX.
     */
    public static final byte STX = 0x02;
    /**
     * The constant FS.
     */
    public static final byte FS = 0x1C;
    /**
     * The constant US.
     */
    public static final byte US = 0x1F;
    /**
     * The constant RS.
     */
    public static final byte RS = 0x1D;
    /**
     * The constant GS.
     */
    public static final byte GS = 0x1E;
    /**
     * The constant ETX.
     */
    public static final byte ETX = 0x03;

    static {
        hexStrings = new String[256];
        for (int i = 0; i < 256; i++) {
            StringBuilder d = new StringBuilder(2);
            char ch = Character.forDigit((byte) i >> 4 & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            ch = Character.forDigit((byte) i & 0x0F, 16);
            d.append(Character.toUpperCase(ch));
            hexStrings[i] = d.toString();
        }

    }

    /**
     * Instantiates a new Iso util.
     */
    public ISOUtil() {
        super();
    }

    /**
     * Ebcdic to ascii string.
     *
     * @param e the e
     * @return the string
     */
    public static String ebcdicToAscii(byte[] e) {
        return EBCDIC.decode(ByteBuffer.wrap(e)).toString();
    }

    /**
     * Ebcdic to ascii string.
     *
     * @param e      the e
     * @param offset the offset
     * @param len    the len
     * @return the string
     */
    public static String ebcdicToAscii(byte[] e, int offset, int len) {
        return EBCDIC.decode(ByteBuffer.wrap(e, offset, len)).toString();
    }

    /**
     * Ebcdic to ascii bytes byte [ ].
     *
     * @param e the e
     * @return the byte [ ]
     */
    public static byte[] ebcdicToAsciiBytes(byte[] e) {
        return ebcdicToAsciiBytes(e, 0, e.length);
    }

    /**
     * Ebcdic to ascii bytes byte [ ].
     *
     * @param e      the e
     * @param offset the offset
     * @param len    the len
     * @return the byte [ ]
     */
    public static byte[] ebcdicToAsciiBytes(byte[] e, int offset, int len) {
        return ebcdicToAscii(e, offset, len).getBytes(CHARSET);
    }

    /**
     * Ascii to ebcdic byte [ ].
     *
     * @param s the s
     * @return the byte [ ]
     */
    public static byte[] asciiToEbcdic(String s) {
        return EBCDIC.encode(s).array();
    }

    /**
     * Ascii to ebcdic byte [ ].
     *
     * @param a the a
     * @return the byte [ ]
     */
    public static byte[] asciiToEbcdic(byte[] a) {
        return EBCDIC.encode(new String(a, CHARSET)).array();
    }

    /**
     * Ascii to ebcdic.
     *
     * @param s      the s
     * @param e      the e
     * @param offset the offset
     */
    public static void asciiToEbcdic(String s, byte[] e, int offset) {
        System.arraycopy(asciiToEbcdic(s), 0, e, offset, s.length());
    }

    /**
     * Ascii to ebcdic.
     *
     * @param s      the s
     * @param e      the e
     * @param offset the offset
     */
    public static void asciiToEbcdic(byte[] s, byte[] e, int offset) {
        asciiToEbcdic(new String(s, CHARSET), e, offset);
    }

    /**
     * Padleft string.
     *
     * @param s   the s
     * @param len the len
     * @param c   the c
     * @return the string
     */
    public static String padleft(String s, int len, char c) {
        s = s.trim();
        if (s.length() > len)
            throw new RuntimeException("invalid len " + s.length() + "/" + len);
        StringBuilder d = new StringBuilder(len);
        int fill = len - s.length();
        while (fill-- > 0)
            d.append(c);
        d.append(s);
        return d.toString();
    }

    /**
     * Padright string.
     *
     * @param s   the s
     * @param len the len
     * @param c   the c
     * @return the string
     */
    public static String padright(String s, int len, char c) {
        s = s.trim();
        if (s.length() > len)
            throw new RuntimeException("invalid len " + s.length() + "/" + len);
        StringBuilder d = new StringBuilder(len);
        int fill = len - s.length();
        d.append(s);
        while (fill-- > 0)
            d.append(c);
        return d.toString();
    }

    /**
     * Trim string.
     *
     * @param s the s
     * @return the string
     */
    public static String trim(String s) {
        return s != null ? s.trim() : null;
    }

    /**
     * Zeropad string.
     *
     * @param s   the s
     * @param len the len
     * @return the string
     */
    public static String zeropad(String s, int len) {
        return padleft(s, len, '0');
    }

    /**
     * Zeropad string.
     *
     * @param l   the l
     * @param len the len
     * @return the string
     */
    public static String zeropad(long l, int len) {

        return padleft(Long.toString((long) (l % Math.pow(10, len))), len, '0');


    }

    /**
     * Strpad string.
     *
     * @param s   the s
     * @param len the len
     * @return the string
     */
    public static String strpad(String s, int len) {
        StringBuilder d = new StringBuilder(s);
        while (d.length() < len)
            d.append(' ');
        return d.toString();
    }

    /**
     * Zeropad right string.
     *
     * @param s   the s
     * @param len the len
     * @return the string
     */
    public static String zeropadRight(String s, int len) {
        StringBuilder d = new StringBuilder(s);
        while (d.length() < len)
            d.append('0');
        return d.toString();
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
     * Str 2 bcd byte [ ].
     *
     * @param s       the s
     * @param padLeft the pad left
     * @return the byte [ ]
     */
    public static byte[] str2bcd(String s, boolean padLeft) {
        int len = s.length();
        byte[] d = new byte[len + 1 >> 1];
        return str2bcd(s, padLeft, d, 0);
    }

    /**
     * Str 2 bcd byte [ ].
     *
     * @param s       the s
     * @param padLeft the pad left
     * @param fill    the fill
     * @return the byte [ ]
     */
    public static byte[] str2bcd(String s, boolean padLeft, byte fill) {
        int len = s.length();
        byte[] d = new byte[len + 1 >> 1];
        if (d.length > 0) {
            if (padLeft)
                d[0] = (byte) ((fill & 0xF) << 4);
            int start = (len & 1) == 1 && padLeft ? 1 : 0;
            int i;
            for (i = start; i < len + start; i++)
                d[i >> 1] |= s.charAt(i - start) - '0' << ((i & 1) == 1 ? 0 : 4);
            if ((i & 1) == 1)
                d[i >> 1] |= fill & 0xF;
        }
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
     * Bcd 2 char array char [ ].
     *
     * @param b       the b
     * @param offset  the offset
     * @param len     the len
     * @param padLeft the pad left
     * @return the char [ ]
     */
    public static char[] bcd2charArray(byte[] b, int offset,
                                       int len, boolean padLeft) {
        char[] d = new char[len];
        int start = (len & 1) == 1 && padLeft ? 1 : 0;
        int ii = 0;
        for (int i = start; i < len + start; i++) {
            int shift = (i & 1) == 1 ? 0 : 4;
            char c = Character.forDigit(
                    b[offset + (i >> 1)] >> shift & 0x0F, 16);
            if (c == 'd')
                c = '=';
            d[ii++] = Character.toUpperCase(c);
        }
        return d;
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
     * Hex 2 char array char [ ].
     *
     * @param b       the b
     * @param offset  the offset
     * @param len     the len
     * @param padLeft the pad left
     * @return the char [ ]
     */
    public static char[] hex2CharArray(byte[] b, int offset,
                                       int len, boolean padLeft) {
        char[] d = new char[len];
        int start = (len & 1) == 1 && padLeft ? 1 : 0;
        int ii = 0;
        for (int i = start; i < len + start; i++) {
            int shift = (i & 1) == 1 ? 0 : 4;
            char c = Character.forDigit(
                    b[offset + (i >> 1)] >> shift & 0x0F, 16);
            d[ii++] = Character.toUpperCase(c);
        }
        return d;
    }

    /**
     * Hex string string.
     *
     * @param b the b
     * @return the string
     */
    public static String hexString(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            d.append(hexStrings[(int) aB & 0xFF]);
        }
        return d.toString();
    }


    /**
     * Hex string string.
     *
     * @param b      the b
     * @param offset the offset
     * @param len    the len
     * @return the string
     */
    public static String hexString(byte[] b, int offset, int len) {
        StringBuilder d = new StringBuilder(len * 2);
        len += offset;
        for (int i = offset; i < len; i++) {
            d.append(hexStrings[(int) b[i] & 0xFF]);
        }
        return d.toString();
    }

    /**
     * Bit set 2 string string.
     *
     * @param b the b
     * @return the string
     */
    public static String bitSet2String(BitSet b) {
        int len = b.size();                             // BBB Should be length()?
        len = len > 128 ? 128 : len;                     // BBB existence of 3rd bitmap not considered here
        StringBuilder d = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            d.append(b.get(i) ? '1' : '0');
        return d.toString();
    }

    /**
     * Bit set 2 byte byte [ ].
     *
     * @param b the b
     * @return the byte [ ]
     */
    public static byte[] bitSet2byte(BitSet b) {
        int len = b.length() + 62 >> 6 << 6;        // +62 because we don't use bit 0 in the BitSet
        byte[] d = new byte[len >> 3];
        for (int i = 0; i < len; i++)
            if (b.get(i + 1))                     // +1 because we don't use bit 0 of the BitSet
                d[i >> 3] |= 0x80 >> i % 8;
        if (len > 64)
            d[0] |= 0x80;
        if (len > 128)
            d[8] |= 0x80;
        return d;
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
     * Bit set 2 int int.
     *
     * @param bs the bs
     * @return the int
     */
    /*
     * Convert BitSet to int value.
     */
    public static int bitSet2Int(BitSet bs) {
        int total = 0;
        int b = bs.length() - 1;
        if (b > 0) {
            int value = (int) Math.pow(2, b);
            for (int i = 0; i <= b; i++) {
                if (bs.get(i))
                    total += value;

                value = value >> 1;
            }
        }

        return total;
    }

    /**
     * Int 2 bit set bit set.
     *
     * @param value the value
     * @return the bit set
     */
    /*
     * Convert int value to BitSet.
     */
    public static BitSet int2BitSet(int value) {

        return int2BitSet(value, 0);
    }

    /**
     * Int 2 bit set bit set.
     *
     * @param value  the value
     * @param offset the offset
     * @return the bit set
     */
    /*
     * Convert int value to BitSet.
     */
    public static BitSet int2BitSet(int value, int offset) {

        BitSet bs = new BitSet();

        String hex = Integer.toHexString(value);
        hex2BitSet(bs, hex.getBytes(), offset);

        return bs;
    }

    /**
     * Byte 2 bit set bit set.
     *
     * @param b                    the b
     * @param offset               the offset
     * @param bitZeroMeansExtended the bit zero means extended
     * @return the bit set
     */
    public static BitSet byte2BitSet
    (byte[] b, int offset, boolean bitZeroMeansExtended) {
        int len = bitZeroMeansExtended ?
                (b[offset] & 0x80) == 0x80 ? 128 : 64 : 64;
        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++)
            if ((b[offset + (i >> 3)] & 0x80 >> i % 8) > 0)
                bmap.set(i + 1);
        return bmap;
    }

    /**
     * Byte 2 bit set bit set.
     *
     * @param b       the b
     * @param offset  the offset
     * @param maxBits the max bits
     * @return the bit set
     */
    public static BitSet byte2BitSet(byte[] b, int offset, int maxBits) {
        boolean b1 = (b[offset] & 0x80) == 0x80;
        boolean b65 = (b.length > offset + 8) && ((b[offset + 8] & 0x80) == 0x80);

        int len = (maxBits > 128 && b1 && b65) ? 192 :
                (maxBits > 64 && b1) ? 128 :
                        (maxBits < 64) ? maxBits : 64;

        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++)
            if ((b[offset + (i >> 3)] & 0x80 >> i % 8) > 0)
                bmap.set(i + 1);
        return bmap;
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
     * Byte 2 bit set bit set.
     *
     * @param bmap      the bmap
     * @param b         the b
     * @param bitOffset the bit offset
     * @return the bit set
     */
    public static BitSet byte2BitSet(BitSet bmap, byte[] b, int bitOffset) {
        int len = b.length << 3;
        for (int i = 0; i < len; i++)
            if ((b[i >> 3] & 0x80 >> i % 8) > 0)
                bmap.set(bitOffset + i + 1);
        return bmap;
    }

    /**
     * Hex 2 bit set bit set.
     *
     * @param b                    the b
     * @param offset               the offset
     * @param bitZeroMeansExtended the bit zero means extended
     * @return the bit set
     */
    public static BitSet hex2BitSet
    (byte[] b, int offset, boolean bitZeroMeansExtended) {
        int len = bitZeroMeansExtended ?
                (Character.digit((char) b[offset], 16) & 0x08) == 8 ? 128 : 64 :
                64;
        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++) {
            int digit = Character.digit((char) b[offset + (i >> 2)], 16);
            if ((digit & 0x08 >> i % 4) > 0)
                bmap.set(i + 1);
        }
        return bmap;
    }

    /**
     * Hex 2 bit set bit set.
     *
     * @param b       the b
     * @param offset  the offset
     * @param maxBits the max bits
     * @return the bit set
     */
    public static BitSet hex2BitSet(byte[] b, int offset, int maxBits) {
        int len = maxBits > 64 ?
                (Character.digit((char) b[offset], 16) & 0x08) == 8 ? 128 : 64 :
                maxBits;
        if (len > 64 && maxBits > 128 &&
                b.length > offset + 16 &&
                (Character.digit((char) b[offset + 16], 16) & 0x08) == 8) {
            len = 192;
        }
        BitSet bmap = new BitSet(len);
        for (int i = 0; i < len; i++) {
            int digit = Character.digit((char) b[offset + (i >> 2)], 16);
            if ((digit & 0x08 >> i % 4) > 0) {
                bmap.set(i + 1);
                if (i == 65 && maxBits > 128)     // BBB this is redundant (check already done outside
                    len = 192;                  // BBB of the loop), but I'll leave it for now..
            }
        }
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
     * Hex 2 bit set bit set.
     *
     * @param bmap      the bmap
     * @param b         the b
     * @param bitOffset the bit offset
     * @return the bit set
     */
    public static BitSet hex2BitSet(BitSet bmap, byte[] b, int bitOffset) {
        int len = b.length << 2;
        for (int i = 0; i < len; i++) {
            int digit = Character.digit((char) b[i >> 2], 16);
            if ((digit & 0x08 >> i % 4) > 0)
                bmap.set(bitOffset + i + 1);
        }
        return bmap;
    }

    /**
     * Hex 2 byte byte [ ].
     *
     * @param b      the b
     * @param offset the offset
     * @param len    the len
     * @return the byte [ ]
     */
    public static byte[] hex2byte(byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i = 0; i < len * 2; i++) {
            int shift = i % 2 == 1 ? 0 : 4;
            d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
        }
        return d;
    }

    /**
     * Hex 2 byte byte [ ].
     *
     * @param s the s
     * @return the byte [ ]
     */
    public static byte[] hex2byte(String s) {
        if (s.length() % 2 == 0) {
            return hex2byte(s.getBytes(), 0, s.length() >> 1);
        } else {
            // Padding left zero to make it even size #Bug raised by tommy
            return hex2byte("0" + s);
        }
    }

    /**
     * Byte 2 hex string.
     *
     * @param bs the bs
     * @return the string
     */
    public static String byte2hex(byte[] bs) {
        return byte2hex(bs, 0, bs.length);
    }

    /**
     * Int 2 byte byte [ ].
     *
     * @param value the value
     * @return the byte [ ]
     */
    public static byte[] int2byte(int value) {
        if (value < 0) {
            return new byte[]{(byte) (value >>> 24 & 0xFF), (byte) (value >>> 16 & 0xFF),
                    (byte) (value >>> 8 & 0xFF), (byte) (value & 0xFF)};
        } else if (value <= 0xFF) {
            return new byte[]{(byte) (value & 0xFF)};
        } else if (value <= 0xFFFF) {
            return new byte[]{(byte) (value >>> 8 & 0xFF), (byte) (value & 0xFF)};
        } else if (value <= 0xFFFFFF) {
            return new byte[]{(byte) (value >>> 16 & 0xFF), (byte) (value >>> 8 & 0xFF),
                    (byte) (value & 0xFF)};
        } else {
            return new byte[]{(byte) (value >>> 24 & 0xFF), (byte) (value >>> 16 & 0xFF),
                    (byte) (value >>> 8 & 0xFF), (byte) (value & 0xFF)};
        }
    }

    /**
     * Byte 2 int int.
     *
     * @param bytes the bytes
     * @return the int
     */
    public static int byte2int(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return 0;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        for (int i = 0; i < 4 - bytes.length; i++) {
            byteBuffer.put((byte) 0);
        }
        for (int i = 0; i < bytes.length; i++) {
            byteBuffer.put(bytes[i]);
        }
        byteBuffer.position(0);
        return byteBuffer.getInt();
    }

    /**
     * Byte 2 hex string.
     *
     * @param bs     the bs
     * @param off    the off
     * @param length the length
     * @return the string
     */
    public static String byte2hex(byte[] bs, int off, int length) {
        if (bs.length <= off || bs.length < off + length)
            throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(length * 2);
        byte2hexAppend(bs, off, length, sb);
        return sb.toString();
    }

    private static void byte2hexAppend(byte[] bs, int off, int length, StringBuilder sb) {
        if (bs.length <= off || bs.length < off + length)
            throw new IllegalArgumentException();
        sb.ensureCapacity(sb.length() + length * 2);
        for (int i = off; i < off + length; i++) {
            sb.append(Character.forDigit(bs[i] >>> 4 & 0xf, 16));
            sb.append(Character.forDigit(bs[i] & 0xf, 16));
        }
    }

    /**
     * Format double string.
     *
     * @param d   the d
     * @param len the len
     * @return the string
     */
    public static String formatDouble(double d, int len) {
        String prefix = Long.toString((long) d);
        String suffix = Integer.toString(
                (int) (Math.round(d * 100f) % 100));
        if (len > 3)
            prefix = ISOUtil.padleft(prefix, len - 3, ' ');
        suffix = ISOUtil.zeropad(suffix, 2);
        return prefix + "." + suffix;
    }

    /**
     * Format amount string.
     *
     * @param l   the l
     * @param len the len
     * @return the string
     */
    public static String formatAmount(long l, int len) {
        String buf = Long.toString(l);
        if (l < 100)
            buf = zeropad(buf, 3);
        StringBuilder s = new StringBuilder(padleft(buf, len - 1, ' '));
        s.insert(len - 3, '.');
        return s.toString();
    }

    /**
     * Normalize string.
     *
     * @param s         the s
     * @param canonical the canonical
     * @return the string
     */
    public static String normalize(String s, boolean canonical) {
        StringBuilder str = new StringBuilder();

        int len = s != null ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '<':
                    str.append("&lt;");
                    break;
                case '>':
                    str.append("&gt;");
                    break;
                case '&':
                    str.append("&amp;");
                    break;
                case '"':
                    str.append("&quot;");
                    break;
                case '\r':
                case '\n':
                    if (canonical) {
                        str.append("&#");
                        str.append(Integer.toString(ch & 0xFF));
                        str.append(';');
                        break;
                    }
                    // else, default append char
                default:
                    if (ch < 0x20) {
                        str.append("&#");
                        str.append(Integer.toString(ch & 0xFF));
                        str.append(';');
                    } else if (ch > 0xff00) {
                        str.append((char) (ch & 0xFF));
                    } else
                        str.append(ch);
            }
        }
        return str.toString();
    }

    /**
     * Normalize string.
     *
     * @param s the s
     * @return the string
     */
    public static String normalize(String s) {
        return normalize(s, true);
    }

    /**
     * Protect string.
     *
     * @param s the s
     * @return the string
     */
    public static String protect(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length();
        int clear = len > 6 ? 6 : 0;
        int lastFourIndex = -1;
        if (clear > 0) {
            lastFourIndex = s.indexOf('=') - 4;
            if (lastFourIndex < 0)
                lastFourIndex = s.indexOf('^') - 4;
            if (lastFourIndex < 0 && s.indexOf('^') < 0)
                lastFourIndex = s.indexOf('D') - 4;
            if (lastFourIndex < 0)
                lastFourIndex = len - 4;
        }
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == '=' || s.charAt(i) == 'D' && s.indexOf('^') < 0)
                clear = 1;  // use clear=5 to keep the expiration date
            else if (s.charAt(i) == '^') {
                lastFourIndex = 0;
                clear = len - i;
            } else if (i == lastFourIndex)
                clear = 4;
            sb.append(clear-- > 0 ? s.charAt(i) : '_');
        }
        s = sb.toString();

        //Addresses Track1 Truncation
        int charCount = s.replaceAll("[^\\^]", "").length();
        if (charCount == 2) {
            s = s.substring(0, s.lastIndexOf("^") + 1);
            s = ISOUtil.padright(s, len, '_');
        }

        return s;
    }

    /**
     * To int array int [ ].
     *
     * @param s the s
     * @return the int [ ]
     */
    public static int[] toIntArray(String s) {
        StringTokenizer st = new StringTokenizer(s);
        int[] array = new int[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++)
            array[i] = Integer.parseInt(st.nextToken());
        return array;
    }

    /**
     * To string array string [ ].
     *
     * @param s the s
     * @return the string [ ]
     */
    public static String[] toStringArray(String s) {
        StringTokenizer st = new StringTokenizer(s);
        String[] array = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++)
            array[i] = st.nextToken();
        return array;
    }

    /**
     * Xor byte [ ].
     *
     * @param op1 the op 1
     * @param op2 the op 2
     * @return the byte [ ]
     */
    public static byte[] xor(byte[] op1, byte[] op2) {
        byte[] result;
        // Use the smallest array
        if (op2.length > op1.length) {
            result = new byte[op1.length];
        } else {
            result = new byte[op2.length];
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (op1[i] ^ op2[i]);
        }
        return result;
    }

    /**
     * Xor byte [ ].
     *
     * @param op1 the op 1
     * @param op2 the op 2
     * @return the byte [ ]
     */
    public static byte[] xor(byte[] op1, byte op2) {
        byte[] result;
        result = new byte[op1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (op1[i] ^ op2);
        }
        return result;
    }

    /**
     * Hexor string.
     *
     * @param op1 the op 1
     * @param op2 the op 2
     * @return the string
     */
    public static String hexor(String op1, String op2) {
        byte[] xor = xor(hex2byte(op1), hex2byte(op2));
        return hexString(xor);
    }

    /**
     * Trim byte [ ].
     *
     * @param array  the array
     * @param length the length
     * @return the byte [ ]
     */
    public static byte[] trim(byte[] array, int length) {
        byte[] trimmedArray = new byte[length];
        System.arraycopy(array, 0, trimmedArray, 0, length);
        return trimmedArray;
    }

    /**
     * Concat byte [ ].
     *
     * @param array1 the array 1
     * @param array2 the array 2
     * @return the byte [ ]
     */
    public static byte[] concat(byte[] array1, byte[] array2) {
        byte[] concatArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, concatArray, 0, array1.length);
        System.arraycopy(array2, 0, concatArray, array1.length, array2.length);
        return concatArray;
    }

    /**
     * Concat byte [ ].
     *
     * @param array1      the array 1
     * @param beginIndex1 the begin index 1
     * @param length1     the length 1
     * @param array2      the array 2
     * @param beginIndex2 the begin index 2
     * @param length2     the length 2
     * @return the byte [ ]
     */
    public static byte[] concat(byte[] array1, int beginIndex1, int length1, byte[] array2,
                                int beginIndex2, int length2) {
        byte[] concatArray = new byte[length1 + length2];
        System.arraycopy(array1, beginIndex1, concatArray, 0, length1);
        System.arraycopy(array2, beginIndex2, concatArray, length1, length2);
        return concatArray;
    }

    /**
     * Sleep.
     *
     * @param millis the millis
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Zero un pad string.
     *
     * @param s the s
     * @return the string
     */
    public static String zeroUnPad(String s) {
        return unPadLeft(s, '0');
    }

    /**
     * Blank un pad string.
     *
     * @param s the s
     * @return the string
     */
    public static String blankUnPad(String s) {
        return unPadRight(s, ' ');
    }

    /**
     * Un pad right string.
     *
     * @param s the s
     * @param c the c
     * @return the string
     */
    public static String unPadRight(String s, char c) {
        int end = s.length();
        if (end == 0)
            return s;
        while (0 < end && s.charAt(end - 1) == c) end--;
        return 0 < end ? s.substring(0, end) : s.substring(0, 1);
    }

    /**
     * Un pad left string.
     *
     * @param s the s
     * @param c the c
     * @return the string
     */
    public static String unPadLeft(String s, char c) {
        int fill = 0, end = s.length();
        if (end == 0)
            return s;
        while (fill < end && s.charAt(fill) == c) fill++;
        return fill < end ? s.substring(fill, end) : s.substring(fill - 1, end);
    }

    /**
     * Is zero boolean.
     *
     * @param s the s
     * @return the boolean
     */
    public static boolean isZero(String s) {
        int i = 0, len = s.length();
        while (i < len && s.charAt(i) == '0') {
            i++;
        }
        return i >= len;
    }

    /**
     * Is blank boolean.
     *
     * @param s the s
     * @return the boolean
     */
    public static boolean isBlank(String s) {
        return s.trim().length() == 0;
    }

    /**
     * Is alpha numeric boolean.
     *
     * @param s the s
     * @return the boolean
     */
    public static boolean isAlphaNumeric(String s) {
        int i = 0, len = s.length();
        while (i < len && (Character.isLetterOrDigit(s.charAt(i)) ||
                s.charAt(i) == ' ' || s.charAt(i) == '.' ||
                s.charAt(i) == '-' || s.charAt(i) == '_')
                || s.charAt(i) == '?') {
            i++;
        }
        return i >= len;
    }

    /**
     * Is numeric boolean.
     *
     * @param s     the s
     * @param radix the radix
     * @return the boolean
     */
    public static boolean isNumeric(String s, int radix) {
        int i = 0, len = s.length();
        while (i < len && Character.digit(s.charAt(i), radix) > -1) {
            i++;
        }
        return i >= len && len > 0;
    }

    /**
     * Bit set 2 extended byte byte [ ].
     *
     * @param b the b
     * @return the byte [ ]
     */
    public static byte[] bitSet2extendedByte(BitSet b) {
        int len = 128;
        byte[] d = new byte[len >> 3];
        for (int i = 0; i < len; i++)
            if (b.get(i + 1))
                d[i >> 3] |= 0x80 >> i % 8;
        d[0] |= 0x80;
        return d;
    }

    /**
     * Parse int int.
     *
     * @param s     the s
     * @param radix the radix
     * @return the int
     * @throws NumberFormatException the number format exception
     */
    public static int parseInt(String s, int radix) throws NumberFormatException {
        int length = s.length();
        if (length > 9)
            throw new NumberFormatException("Number can have maximum 9 digits");
        int result;
        int index = 0;
        int digit = Character.digit(s.charAt(index++), radix);
        if (digit == -1)
            throw new NumberFormatException("String contains non-digit");
        result = digit;
        while (index < length) {
            result *= radix;
            digit = Character.digit(s.charAt(index++), radix);
            if (digit == -1)
                throw new NumberFormatException("String contains non-digit");
            result += digit;
        }
        return result;
    }

    /**
     * Parse int int.
     *
     * @param s the s
     * @return the int
     * @throws NumberFormatException the number format exception
     */
    public static int parseInt(String s) throws NumberFormatException {
        return parseInt(s, 10);
    }

    /**
     * Parse int int.
     *
     * @param cArray the c array
     * @param radix  the radix
     * @return the int
     * @throws NumberFormatException the number format exception
     */
    public static int parseInt(char[] cArray, int radix) throws NumberFormatException {
        int length = cArray.length;
        if (length > 9)
            throw new NumberFormatException("Number can have maximum 9 digits");
        int result;
        int index = 0;
        int digit = Character.digit(cArray[index++], radix);
        if (digit == -1)
            throw new NumberFormatException("Char array contains non-digit");
        result = digit;
        while (index < length) {
            result *= radix;
            digit = Character.digit(cArray[index++], radix);
            if (digit == -1)
                throw new NumberFormatException("Char array contains non-digit");
            result += digit;
        }
        return result;
    }

    /**
     * Parse int int.
     *
     * @param cArray the c array
     * @return the int
     * @throws NumberFormatException the number format exception
     */
    public static int parseInt(char[] cArray) throws NumberFormatException {
        return parseInt(cArray, 10);
    }

    /**
     * Parse int int.
     *
     * @param bArray the b array
     * @param radix  the radix
     * @return the int
     * @throws NumberFormatException the number format exception
     */
    public static int parseInt(byte[] bArray, int radix) throws NumberFormatException {
        int length = bArray.length;
        if (length > 9)
            throw new NumberFormatException("Number can have maximum 9 digits");
        int result;
        int index = 0;
        int digit = Character.digit((char) bArray[index++], radix);
        if (digit == -1)
            throw new NumberFormatException("Byte array contains non-digit");
        result = digit;
        while (index < length) {
            result *= radix;
            digit = Character.digit((char) bArray[index++], radix);
            if (digit == -1)
                throw new NumberFormatException("Byte array contains non-digit");
            result += digit;
        }
        return result;
    }

    /**
     * Parse int int.
     *
     * @param bArray the b array
     * @return the int
     * @throws NumberFormatException the number format exception
     */
    public static int parseInt(byte[] bArray) throws NumberFormatException {
        return parseInt(bArray, 10);
    }

    private static String hexOffset(int i) {
        i = i >> 4 << 4;
        int w = i > 0xFFFF ? 8 : 4;
        return zeropad(Integer.toString(i, 16), w);
    }

    /**
     * Hexdump string.
     *
     * @param b the b
     * @return the string
     */
    public static String hexdump(byte[] b) {
        return hexdump(b, 0, b.length);
    }

    /**
     * Hexdump string.
     *
     * @param b      the b
     * @param offset the offset
     * @return the string
     */
    public static String hexdump(byte[] b, int offset) {
        return hexdump(b, offset, b.length - offset);
    }

    /**
     * Hexdump string.
     *
     * @param b      the b
     * @param offset the offset
     * @param len    the len
     * @return the string
     */
    public static String hexdump(byte[] b, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        StringBuilder hex = new StringBuilder();
        StringBuilder ascii = new StringBuilder();
        String sep = "  ";
        String lineSep = System.getProperty("line.separator");
        len = offset + len;

        for (int i = offset; i < len; i++) {
            hex.append(hexStrings[(int) b[i] & 0xFF]);
            hex.append(' ');
            char c = (char) b[i];
            ascii.append(c >= 32 && c < 127 ? c : '.');

            int j = i % 16;
            switch (j) {
                case 7:
                    hex.append(' ');
                    break;
                case 15:
                    sb.append(hexOffset(i));
                    sb.append(sep);
                    sb.append(hex.toString());
                    sb.append(' ');
                    sb.append(ascii.toString());
                    sb.append(lineSep);
                    hex = new StringBuilder();
                    ascii = new StringBuilder();
                    break;
            }
        }
        if (hex.length() > 0) {
            while (hex.length() < 49)
                hex.append(' ');

            sb.append(hexOffset(len));
            sb.append(sep);
            sb.append(hex.toString());
            sb.append(' ');
            sb.append(ascii.toString());
            sb.append(lineSep);
        }
        return sb.toString();
    }


    /**
     * Decode hex dump byte [ ].
     *
     * @param s the s
     * @return the byte [ ]
     */
    public static byte[] decodeHexDump(String s) {
        return hex2byte(
                Arrays.stream(s.split("\\r\\n|[\\r\\n]"))
                        .map(x ->
                                x.replaceAll("^.{4}  ", "").
                                        replaceAll("\\s\\s", " ").
                                        replaceAll("(([0-9A-F][0-9A-F]\\s){1,16}).*$", "$1").
                                        replaceAll("\\s", "")
                        ).collect(Collectors.joining()));
    }


    /**
     * Strpadf string.
     *
     * @param s   the s
     * @param len the len
     * @return the string
     */
    public static String strpadf(String s, int len) {
        StringBuilder d = new StringBuilder(s);
        while (d.length() < len)
            d.append('F');
        return d.toString();
    }

    /**
     * Trimf string.
     *
     * @param s the s
     * @return the string
     */
    public static String trimf(String s) {
        if (s != null) {
            int l = s.length();
            if (l > 0) {
                while (--l >= 0) {
                    if (s.charAt(l) != 'F')
                        break;
                }
                s = l == 0 ? "" : s.substring(0, l + 1);
            }
        }
        return s;
    }

    /**
     * Take last n string.
     *
     * @param s the s
     * @param n the n
     * @return the string
     */
    public static String takeLastN(String s, int n) {
        if (s.length() > n) {
            return s.substring(s.length() - n);
        } else {
            if (s.length() < n) {
                return zeropad(s, n);
            } else {
                return s;
            }
        }
    }

    /**
     * Take first n string.
     *
     * @param s the s
     * @param n the n
     * @return the string
     */
    public static String takeFirstN(String s, int n) {
        if (s.length() > n) {
            return s.substring(0, n);
        } else {
            if (s.length() < n) {
                return zeropad(s, n);
            } else {
                return s;
            }
        }
    }

    /**
     * Millis to string string.
     *
     * @param millis the millis
     * @return the string
     */
    public static String millisToString(long millis) {
        StringBuilder sb = new StringBuilder();
        if (millis < 0) {
            millis = -millis;
            sb.append('-');
        }
        int ms = (int) (millis % 1000);
        millis /= 1000;
        int dd = (int) (millis / 86400);
        millis -= dd * 86400;
        int hh = (int) (millis / 3600);
        millis -= hh * 3600;
        int mm = (int) (millis / 60);
        millis -= mm * 60;
        int ss = (int) millis;
        if (dd > 0) {
            sb.append(Long.toString(dd));
            sb.append("d ");
        }
        sb.append(zeropad(hh, 2));
        sb.append(':');
        sb.append(zeropad(mm, 2));
        sb.append(':');
        sb.append(zeropad(ss, 2));
        sb.append('.');
        sb.append(zeropad(ms, 3));
        return sb.toString();
    }

    /**
     * Format amount conversion rate string.
     *
     * @param convRate the conv rate
     * @return the string
     */
    public static String formatAmountConversionRate(double convRate) {
        if (convRate == 0)
            return null;
        BigDecimal cr = new BigDecimal(convRate);
        int x = 7 - cr.precision() + cr.scale();
        String bds = cr.movePointRight(cr.scale()).toString();
        if (x > 9)
            bds = ISOUtil.zeropad(bds, bds.length() + x - 9);
        String ret = ISOUtil.zeropadRight(bds, 7);
        return Math.min(9, x) + ISOUtil.takeFirstN(ret, 7);
    }

    /**
     * Parse amount conversion rate double.
     *
     * @param convRate the conv rate
     * @return the double
     */
    public static double parseAmountConversionRate(String convRate) {
        if (convRate == null || convRate.length() != 8)
            throw new IllegalArgumentException("Invalid amount converion rate argument: '" +
                    convRate + "'");
        BigDecimal bd = new BigDecimal(convRate);
        int pow = bd.movePointLeft(7).intValue();
        bd = new BigDecimal(convRate.substring(1));
        return bd.movePointLeft(pow).doubleValue();
    }

    /**
     * Comma encode string.
     *
     * @param ss the ss
     * @return the string
     */
    public static String commaEncode(String[] ss) {
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            if (sb.length() > 0)
                sb.append(',');
            if (s != null) {
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    switch (c) {
                        case '\\':
                        case ',':
                            sb.append('\\');
                            break;
                    }
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Comma decode string [ ].
     *
     * @param s the s
     * @return the string [ ]
     */
    public static String[] commaDecode(String s) {
        List<String> l = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!escaped) {
                switch (c) {
                    case '\\':
                        escaped = true;
                        continue;
                    case ',':
                        l.add(sb.toString());
                        sb = new StringBuilder();
                        continue;
                }
            }
            sb.append(c);
            escaped = false;
        }
        if (sb.length() > 0)
            l.add(sb.toString());
        return l.toArray(new String[l.size()]);
    }

    /**
     * Comma decode string.
     *
     * @param s the s
     * @param i the
     * @return the string
     */
    public static String commaDecode(String s, int i) {
        String[] ss = commaDecode(s);
        int l = ss.length;
        return i >= 0 && i < l ? ss[i] : null;
    }

    /**
     * Calc luhn char.
     *
     * @param p the p
     * @return the char
     */
    public static char calcLUHN(String p) {
        int i, crc;
        int odd = p.length() % 2;

        for (i = crc = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Invalid PAN " + p);
            }
            c = (char) (c - '0');
            if (i % 2 != odd)
                crc += c * 2 >= 10 ? c * 2 - 9 : c * 2;
            else
                crc += c;
        }

        return (char) ((crc % 10 == 0 ? 0 : 10 - crc % 10) + '0');
    }

    /**
     * Gets random digits.
     *
     * @param r     the r
     * @param l     the l
     * @param radix the radix
     * @return the random digits
     */
    public static String getRandomDigits(Random r, int l, int radix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l; i++) {
            sb.append(r.nextInt(radix));
        }
        return sb.toString();
    }

    /**
     * Readable file size string.
     *
     * @param size the size
     * @return the string
     */
// See http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
    // and http://physics.nist.gov/cuu/Units/binary.html
    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"Bi", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
