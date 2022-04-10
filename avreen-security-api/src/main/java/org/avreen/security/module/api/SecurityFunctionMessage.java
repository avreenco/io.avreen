package org.avreen.security.module.api;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Security function message.
 */
public class SecurityFunctionMessage {
    private byte[] header;

    /**
     * Instantiates a new Security function message.
     */
    public SecurityFunctionMessage() {
        super();
    }


    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        return fields;
    }

    private static byte[] encodeToByteArray(Object obj) {
        if (obj instanceof byte[])
            return (byte[]) obj;
        if (obj instanceof String)
            return SecurityUtil.hex2byte(obj.toString());
        if (obj instanceof Byte)
            return new byte[]{(byte) obj};
        if (obj instanceof IByteArrayConverter)
            return ((IByteArrayConverter) obj).encode();
        if (obj instanceof Number)
            return SecurityUtil.hex2byte(obj.toString());
        throw new RuntimeException("not support to byte array converter");
    }


    /**
     * Get header byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getHeader() {
        return header;
    }

    /**
     * Sets header.
     *
     * @param header the header
     */
    public void setHeader(byte[] header) {
        this.header = header;
    }

    /**
     * Dump.
     *
     * @param p      the p
     * @param indent the indent
     */
    public void dump(PrintStream p, String indent) {
        p.println(indent + "----------------------------------------");
        try {
            List<Field> allFields = new ArrayList<>();
            getAllFields(allFields, getClass());
            for (Field f : allFields) {
                f.setAccessible(true);
                if (Modifier.isStatic(f.getModifiers()))
                    continue;
                SafenetItemAnnotaion hsmField = f.getAnnotation(SafenetItemAnnotaion.class);
                p.print("[ ");
                if (hsmField != null) {
                    if (hsmField.type().equals(SafenetFieldType.h)) {
                        if (hsmField.length() > 0)
                            p.print("hex " + SecurityUtil.padleft(new Integer(hsmField.length()).toString(), 4, '0'));
                        else
                            p.print("var     ");
                    } else if (hsmField.type().equals(SafenetFieldType.K_Spec))
                        p.print("key-spec");
                    else
                        p.print("???");
                } else {
                    p.print("trans   ");
                }
                p.print("   ] ");
                Object val = f.get(this);
                if (val != null) {
                    String strVal = SecurityUtil.hexString(encodeToByteArray(val));
                    p.print(indent + "  ");
                    int length = f.getName().length();
                    p.print("'" + f.getName() + "' ");
                    for (int idx = 0; idx < (20 - length); idx++)
                        p.print("-");
                    p.print(">  ");
                    p.print(" [");
                    p.print(strVal);
                    p.println("]");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        PrintStream printWriter = new PrintStream(result);
        this.dump(printWriter, "");
        return result.toString();
    }

}
