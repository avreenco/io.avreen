package org.avreen.security.module.api;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Pin format.
 */
public class PinFormat implements IByteArrayConverter {
    /**
     * The constant ANSI.
     */
    public static final PinFormat ANSI = new PinFormat("00");
    /**
     * The constant Docutel_2.
     */
    public static final PinFormat Docutel_2 = new PinFormat("02");
    /**
     * The constant PIN_Pad.
     */
    public static final PinFormat PIN_Pad = new PinFormat("03");
    /**
     * The constant Docutel.
     */
    public static final PinFormat Docutel = new PinFormat("08");
    /**
     * The constant ZKA.
     */
    public static final PinFormat ZKA = new PinFormat("09");
    /**
     * The constant ISO_0.
     */
    public static final PinFormat ISO_0 = new PinFormat("10");
    /**
     * The constant ISO_1.
     */
    public static final PinFormat ISO_1 = new PinFormat("11");
    /**
     * The constant ISO_2.
     */
    public static final PinFormat ISO_2 = new PinFormat("12");
    /**
     * The constant ISO_30.
     */
    public static final PinFormat ISO_30 = new PinFormat("13");
    private String value;

    private PinFormat(String value) {
        this.value = value;
    }

    /**
     * Instantiates a new Pin format.
     */
    public PinFormat() {

    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getValue() == ((PinFormat) obj).getValue();
    }

    @Override
    public byte[] encode() {
        return SecurityUtil.hex2byte(getValue());

    }

    @Override
    public void decode(byte[] encoded) {
        value = SecurityUtil.hexString(encoded);
    }
}
