package org.avreen.security.module.impl.hsm.safenet.engine;

import org.avreen.security.module.api.SecurityUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Safe net variable length util.
 */
public class SafeNetVariableLengthUtil {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        for (int idx = 0; idx < 1; idx++) {
            test(true);
            test(false);
        }
    }

    /**
     * Test.
     *
     * @param isDirect the is direct
     * @throws Exception the exception
     */
    public static void test(boolean isDirect) throws Exception {

        ByteBuffer byteBuffer = null;
        if (isDirect)
            byteBuffer = ByteBuffer.allocateDirect(4);
        else
            byteBuffer = ByteBuffer.allocate(4);
        long start = System.currentTimeMillis();
        System.out.println();
        for (int len = 1; len < 268435455; len++) {
            if ((len % 10000000) == 0)
                System.out.print("*");
            byteBuffer.position(0);
            encodeLength(byteBuffer, len);
            byteBuffer.position(0);
            //System.out.println(EMVFunctionUtil.hexString(b));
            //byte[]   b = ByteUtil.hex2byte("7F");
            int dLen = decodeLength(byteBuffer);
            if (len != dLen) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! len=" + len);
                break;
            }
        }
        System.out.println();
        long end = System.currentTimeMillis();
        if (isDirect)
            System.out.println("OK in Direct Mode" + (end - start) + " (ms) time");
        else
            System.out.println("OK in NoneDirect Mode" + (end - start) + " (ms) time");


    }

    /**
     * Hex 2 byte len int.
     *
     * @param len the len
     * @return the int
     */
    public static int hex2byteLen(int len) {
        return (len % 2) == 0 ? len >> 1 : (len + 1) >> 1;
    }

    /**
     * Gets packed length.
     *
     * @param length the length
     * @return the packed length
     */
    public static int getPackedLength(int length) {
        int len_of_Length = -1;

        if (length >= 0 && length <= 127)
            len_of_Length = 1;
        else if (length >= 0 && length <= 16383)
            len_of_Length = 2;
        else if (length >= 0 && length <= 2097151)
            len_of_Length = 3;
        else if (length >= 0 && length <= 268435455)
            len_of_Length = 4;

        return len_of_Length;
    }

    /**
     * Decode length int.
     *
     * @param byteBuffer the byte buffer
     * @return the int
     */
    public static int decodeLength(ByteBuffer byteBuffer) {
        ArrayList<Byte> lenBytes = new ArrayList<Byte>();
        byte byte1 = byteBuffer.get();
        lenBytes.add(byte1);
        boolean len4 = (byte1 & 0xE0) == 0xE0;
        boolean len3 = (byte1 & 0xC0) == 0xC0;
        boolean len2 = (byte1 & 0x80) == 0x80;
        if (len4) {
            lenBytes.clear();
            lenBytes.add((byte) (byte1 & (byte) 0x1F));
            lenBytes.add((byteBuffer.get()));
            lenBytes.add((byteBuffer.get()));
            lenBytes.add((byteBuffer.get()));
        } else if (len3) {
            lenBytes.clear();
            lenBytes.add((byte) (byte1 & (byte) 0x3F));
            lenBytes.add((byteBuffer.get()));
            lenBytes.add((byteBuffer.get()));

        } else if (len2) {
            lenBytes.clear();
            lenBytes.add((byte) (byte1 & (byte) 0x7F));
            lenBytes.add((byteBuffer.get()));
        } else {
            /* nothing */
        }


        byte[] lenArray = new byte[lenBytes.size()];
        for (int idx = 0; idx < lenArray.length; idx++)
            lenArray[idx] = lenBytes.get(idx);
        String hexVal = SecurityUtil.hexString(lenArray);
        return Integer.parseInt(hexVal, 16);

    }

    /**
     * Encode length int.
     *
     * @param byteBuffer the byte buffer
     * @param length     the length
     * @return the int
     */
    public static int encodeLength(ByteBuffer byteBuffer, int length
    ) {
        int hex_len = hex2byteLen(length * 2);
        int lenPrefixLength = getPackedLength(length);
        if (lenPrefixLength == -1)
            throw new RuntimeException("Length of length is -1, this is wrong. Input length is {} and in hex base is");
        int new_len = hex_len;
        switch (lenPrefixLength) {
            case 2:
                new_len = hex_len + 0x8000;
                break;
            case 3:
                new_len = hex_len + 0xC00000;
                break;
            case 4:
                new_len = hex_len + 0xE0000000;
                break;

        }
        byte[] final_len = SecurityUtil.hex2byte(Integer.toHexString(new_len));
        byteBuffer.put(final_len);
        return lenPrefixLength;
    }

}