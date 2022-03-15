package io.avreen.common.util;

import java.math.BigInteger;

/**
 * The class Codec util.
 */
public class CodecUtil {

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

    public static byte[] str2hex(String s, boolean padLeft, byte[] d, int offset) {
        int len = s.length();
        int start = (len & 1) == 1 && padLeft ? 1 : 0;
        for (int i = start; i < len + start; i++)
            d[offset + (i >> 1)] |= Character.digit(s.charAt(i - start), 16) << ((i & 1) == 1 ? 0 : 4);
        return d;
    }
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
    public static String zeroPad(long l, int len) {

        return padLeft(Long.toString((long) (l % Math.pow(10, len))), len, '0');


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
     * Padleft string.
     *
     * @param s   the s
     * @param len the len
     * @param c   the c
     * @return the string
     */
    public static String padLeft(String s, int len, char c) {
        StringBuilder builder = new StringBuilder(s);
        while (builder.length() < len) {
            builder.insert(0,c);
        }
        return builder.toString();
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
