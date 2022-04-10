package org.avreen.security.module.api;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Select code.
 */
public class SelectCode implements IByteArrayConverter {


    /**
     * The constant Encrypt_Command_Data_Only.
     */
    public static final SelectCode Encrypt_Command_Data_Only = new SelectCode((byte) 01);
    /**
     * The constant Calculate_MAC_for_entire_command.
     */
    public static final SelectCode Calculate_MAC_for_entire_command = new SelectCode((byte) 02);
    /**
     * The constant Encrypt_and_Calculate_MAC.
     */
    public static final SelectCode Encrypt_and_Calculate_MAC = new SelectCode((byte) 03);
    private byte value;

    private SelectCode(byte value) {
        this.value = value;
    }

    /**
     * Instantiates a new Select code.
     */
    public SelectCode() {

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
        return this.getValue() == ((SelectCode) obj).getValue();
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
