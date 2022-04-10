package org.avreen.security.module.api;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Mac algorithm.
 */
public class MACAlgorithm implements IByteArrayConverter {
    /**
     * The constant ISO_9807.
     */
    public static final MACAlgorithm ISO_9807 = new MACAlgorithm((byte) 0x0);
    /**
     * The constant Triple_DES_CBC.
     */
    public static final MACAlgorithm Triple_DES_CBC = new MACAlgorithm((byte) 0x1);

    private byte value;

    private MACAlgorithm(byte value) {
        this.value = value;
    }

    /**
     * Instantiates a new Mac algorithm.
     */
    public MACAlgorithm() {
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
        return this.getValue() == ((MACAlgorithm) obj).getValue();
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
