package org.avreen.security.module.api;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Cipher mode.
 */
public class CipherMode implements IByteArrayConverter {
    /**
     * The constant ECB.
     */
    public static final CipherMode ECB = new CipherMode((byte) 0x00);
    /**
     * The constant CBC.
     */
    public static final CipherMode CBC = new CipherMode((byte) 0x01);
    private byte value;

    private CipherMode(byte value) {
        this.value = value;
    }

    /**
     * Instantiates a new Cipher mode.
     */
    public CipherMode() {
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
        return this.getValue() == ((CipherMode) obj).getValue();
    }


    @Override
    public byte[] encode() {
        return new byte[]{value};
    }

    @Override
    public void decode(byte[] encoded) {
        value = encoded[0];
    }
}
