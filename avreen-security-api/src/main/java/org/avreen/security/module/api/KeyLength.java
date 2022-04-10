package org.avreen.security.module.api;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Key length.
 */
public class KeyLength {
    /**
     * The constant DES.
     */
    public static final KeyLength DES = new KeyLength((short) 64);
    /**
     * The constant DES3_2KEY.
     */
    public static final KeyLength DES3_2KEY = new KeyLength((short) 128);
    /**
     * The constant DES3_3KEY.
     */
    public static final KeyLength DES3_3KEY = new KeyLength((short) 192);
    private short val;

    private KeyLength(short val) {
        this.val = val;
    }

    /**
     * Instantiates a new Key length.
     */
    public KeyLength() {

    }

    /**
     * From short key length.
     *
     * @param value the value
     * @return the key length
     */
    public static KeyLength fromShort(short value) {
        if (value == 64)
            return KeyLength.DES;
        else if (value == 128)
            return KeyLength.DES3_2KEY;
        else if (value == 192)
            return KeyLength.DES3_3KEY;
        else
            throw new RuntimeException("invalid key length=" + value);
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public short getValue() {
        return val;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getValue() == ((KeyLength) obj).getValue();
    }

}
