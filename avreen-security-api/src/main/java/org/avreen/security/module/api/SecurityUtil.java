package org.avreen.security.module.api;

import java.nio.ByteBuffer;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Security util.
 */
public class SecurityUtil {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        String bdk = "1355135513551355135513551355";
        String ksn = "12345678901234567890";
        String format20 = createFormat20KeySpec(ksn, bdk);
        System.out.println(format20);
        byte[] ksn2 = getKSN(format20);
        String bdk2 = getBDK(format20);
        System.out.println("ksn=" + hexString(ksn2) + " bdk=" + bdk2);
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
     * Create format 20 key spec string.
     *
     * @param ksn        the ksn
     * @param bdkKeySpec the bdk key spec
     * @return the string
     */
    public static String createFormat20KeySpec(String ksn, String bdkKeySpec) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("20");
        String hexVal = Integer.toHexString(bdkKeySpec.length() / 2);
        buffer.append(padleft(hexVal, 2, '0'));
        buffer.append(bdkKeySpec);
        buffer.append(ksn);
        buffer.append("02");
        return buffer.toString();
    }

    /**
     * Gets bdk.
     *
     * @param format20KeySpec the format 20 key spec
     * @return the bdk
     */
    public static String getBDK(String format20KeySpec) {
        int length = Integer.parseInt(format20KeySpec.substring(2, 4), 16);
        return format20KeySpec.substring(4, 4 + (length * 2));
    }

    /**
     * Is bdk boolean.
     *
     * @param bdkSpec the bdk spec
     * @return the boolean
     */
    public static boolean isBDK(String bdkSpec) {
        return "20".equals(bdkSpec.substring(0,2));

    }

    /**
     * Get ksn byte [ ].
     *
     * @param format20KeySpec the format 20 key spec
     * @return the byte [ ]
     */
    public static byte[] getKSN(String format20KeySpec) {


        return hex2byte(format20KeySpec.substring(format20KeySpec.length() - 22, format20KeySpec.length() - 2));
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
     * Hex string string.
     *
     * @param b the b
     * @return the string
     */
    public static String hexString(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            char hi = Character.forDigit((b[i] >> 4) & 0x0F, 16);
            char lo = Character.forDigit(b[i] & 0x0F, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

    /**
     * Hex string string.
     *
     * @param byteBuffer the byte buffer
     * @return the string
     */
    public static String hexString(ByteBuffer byteBuffer) {
        StringBuilder d = new StringBuilder(byteBuffer.limit() * 2);
        for (int i = 0; i < byteBuffer.limit(); i++) {
            byte by = byteBuffer.get(i);
            char hi = Character.forDigit((by >> 4) & 0x0F, 16);
            char lo = Character.forDigit(by & 0x0F, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

}
