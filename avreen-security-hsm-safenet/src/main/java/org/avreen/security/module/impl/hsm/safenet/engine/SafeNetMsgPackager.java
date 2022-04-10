package org.avreen.security.module.impl.hsm.safenet.engine;

import io.avreen.common.log.LoggerDomain;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.avreen.security.module.api.*;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Safe net msg packager.
 */
public class SafeNetMsgPackager {
    private static InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".security.module.impl.hsm.safenet.engine.SafeNetMsgPackager");

    private static byte[] encodeToByteArray(Object obj) {
        if (obj instanceof byte[])
            return (byte[]) obj;
        if (obj instanceof String)
            return SecurityUtil.hex2byte(obj.toString());
        if (obj instanceof Byte)
            return new byte[]{(byte) obj};
        if (obj instanceof IByteArrayConverter)
            return ((IByteArrayConverter) obj).encode();
        throw new RuntimeException("not support to byte array converter");
    }

    private static Object decodeFromByteArray(byte[] bytes, Class type) {
        if (byte[].class.equals(type))
            return bytes;
        if (byte.class.equals(type))
            return bytes[0];
        if (IByteArrayConverter.class.isAssignableFrom(type)) {
            try {
                IByteArrayConverter iByteArrayConverter = ((IByteArrayConverter) type.newInstance());
                iByteArrayConverter.decode(bytes);
                return iByteArrayConverter;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        if (String.class.equals(type))
            return SecurityUtil.hexString(bytes);
        throw new RuntimeException("not support to byte array converter");
    }


    /**
     * Encode int.
     *
     * @param byteBuffer the byte buffer
     * @param msg        the msg
     * @return the int
     * @throws Exception the exception
     */
    public static int encode(ByteBuffer byteBuffer, SecurityFunctionMessage msg) throws Exception {

        int pos = byteBuffer.position();
        byteBuffer.position(pos + 6);
        int len = 0;
        {
            SafenetFunctionCodeAnnotaion annotation = msg.getClass().getAnnotation(SafenetFunctionCodeAnnotaion.class);
            byte[] fcBytes = SecurityUtil.hex2byte(annotation.code());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("encode: Function Code bytes={}", SecurityUtil.hexString(fcBytes));
            byteBuffer.put(fcBytes);
            len += fcBytes.length;
        }
        Field[] allFields = msg.getClass().getDeclaredFields();
        Arrays.sort(allFields, new HSMFieldComaprer());
        for (Field f : allFields) {
            SafenetItemAnnotaion hsmField = f.getAnnotation(SafenetItemAnnotaion.class);
            if (hsmField == null)
                continue;
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("encode: order={} name={} length={}  type={}", f.getName(), hsmField.order(), hsmField.length(), hsmField.type());
            f.setAccessible(true);
            Object val = f.get(msg);
            if (val == null) {
//                System.out.println("field name=" + f.getName() + " value=null");
            } else {
                byte[] bVal = encodeToByteArray(val);
                if (hsmField.length() == -1) {
                    len += SafeNetVariableLengthUtil.encodeLength(byteBuffer, bVal.length);
                }
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("encode: order={} name={} length={}  type={} bytes={}", f.getName(), hsmField.order(), hsmField.length(), hsmField.type(), SecurityUtil.hexString(bVal));
                byteBuffer.put(bVal);
                len += bVal.length;
            }
        }
        byteBuffer.position(pos);
        {
            byte[] msgLenHex = SecurityUtil.hex2byte(SecurityUtil.padleft(Integer.toHexString(len), 4, '0'));
            byte[] header = msg.getHeader();
            msg.setHeader(header);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("encode: header bytes={}", SecurityUtil.hexString(header));
            byteBuffer.put(header);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("encode: length bytes={}", SecurityUtil.hexString(msgLenHex));
            byteBuffer.put(msgLenHex);
        }
        return len + 6;
    }

    /**
     * Decode header byte [ ].
     *
     * @param messageHeader the message header
     * @return the byte [ ]
     * @throws Exception the exception
     */
    public static byte[] decodeHeader(byte[] messageHeader) throws Exception {
        byte[] header = new byte[4];
        System.arraycopy(messageHeader, 0, header, 0, header.length);
        return header;
    }

    /**
     * Decode msg length int.
     *
     * @param messageHeader the message header
     * @return the int
     * @throws Exception the exception
     */
    public static int decodeMsgLength(byte[] messageHeader) throws Exception {
        byte[] msgLen = new byte[2];
        System.arraycopy(messageHeader, 4, msgLen, 0, msgLen.length);
        return Integer.parseInt(SecurityUtil.hexString(msgLen), 16);
    }

    /**
     * Decode fc from body byte [ ].
     *
     * @param byteBuffer the byte buffer
     * @return the byte [ ]
     * @throws Exception the exception
     */
    public static byte[] decodeFCFromBody(ByteBuffer byteBuffer) throws Exception {
        byte[] fc = new byte[3];
        byteBuffer.get(fc);
        return fc;
    }


    /**
     * Decode body int.
     *
     * @param byteBuffer the byte buffer
     * @param msg        the msg
     * @return the int
     * @throws Exception the exception
     */
    public static int decodeBody(ByteBuffer byteBuffer, SecurityFunctionMessage msg) throws Exception {
        Field[] allFields = msg.getClass().getDeclaredFields();
        int total_len = 0;
        Arrays.sort(allFields, new HSMFieldComaprer());
        {
            byte[] fc = new byte[3];
            byteBuffer.get(fc);
            total_len += fc.length;
        }
        boolean unpackBody = true;
        if (msg instanceof ResponseBase) {

            ResponseBase responseBase = (ResponseBase) msg;
            byte[] rc = new byte[1];
            byteBuffer.get(rc);
            responseBase.setReturnCode(SecurityUtil.hexString(rc));
            total_len += rc.length;
            unpackBody = responseBase.isApprove();
        }
        if (!unpackBody)
            return total_len;

        for (Field f : allFields) {
            SafenetItemAnnotaion hsmField = f.getAnnotation(SafenetItemAnnotaion.class);
            if (hsmField == null)
                continue;
            int len = hsmField.length();
            if (hsmField.length() == -1) {
                len = SafeNetVariableLengthUtil.decodeLength(byteBuffer);
                total_len += SafeNetVariableLengthUtil.getPackedLength(len);
            }
            byte[] value = new byte[len];
            byteBuffer.get(value);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("decode: order={} name={} length={}  type={} bytes={}", f.getName(), hsmField.order(), hsmField.length(), hsmField.type(), SecurityUtil.hexString(value));
            total_len += value.length;
            f.setAccessible(true);
            f.set(msg, decodeFromByteArray(value, f.getType()));
        }
        return total_len;
    }


}
