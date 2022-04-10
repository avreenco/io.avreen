package org.avreen.security.module.api;

import java.util.HashMap;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Encryption standard.
 */
public class KVCMethod implements IByteArrayConverter {
    /**
     * The constant DES.
     */
    public static final KVCMethod FORMAT6 = new KVCMethod((byte) 0x01);
    private static HashMap<Byte, String> toStringMap = new HashMap<>();

    static {
        toStringMap.put(FORMAT6.getValue(), "FORMAT6");
    }

    private byte value;

    private KVCMethod(byte value) {
        this.value = value;
    }

    /**
     * Instantiates a new Encryption standard.
     */
    public KVCMethod() {
    }

    /**
     * From value encryption standard.
     *
     * @param value the value
     * @return the encryption standard
     */
    public static final KVCMethod fromValue(byte value) {
        return new KVCMethod(value);
    }

    /**
     * From value encryption standard.
     *
     * @param value the value
     * @return the encryption standard
     */
    public static final KVCMethod fromValue(String value) {
        for (byte k : toStringMap.keySet()) {
            if (toStringMap.get(k).equals(value))
                return fromValue(k);
        }
        throw new RuntimeException("can not found encryption standard from string value" + value);
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public byte getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getValue() == ((KVCMethod) obj).getValue();
    }

    @Override
    public byte[] encode() {
        return new byte[]{value};
    }

    @Override
    public void decode(byte[] encoded) {
        value = encoded[0];
    }

    @Override
    public String toString() {
        if (toStringMap.containsKey(value))
            return toStringMap.get(value);
        return "FORMAT" + value;
    }

}
