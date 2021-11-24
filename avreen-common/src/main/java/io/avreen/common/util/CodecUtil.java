package io.avreen.common.util;

/**
 * The class Codec util.
 */
public class CodecUtil {
    /**
     * The constant hexStrings.
     */
    public static final String[] hexStrings;

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

}
