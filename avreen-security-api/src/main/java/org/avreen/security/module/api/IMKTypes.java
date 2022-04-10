package org.avreen.security.module.api;

/*
    Page 54 of document
    SafeNet Luna EFT (PH-EFT) Programmer's Guide_PN007-003198-002_RevAG1.pdf
    PVK Value not found in document??!!

 */

import java.util.HashMap;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Key types.
 */
public class IMKTypes implements IByteArrayConverter {
    /**
     * The constant DPK.
     */
    public static final IMKTypes AC = new IMKTypes((byte) 1);
    /**
     * The constant PPK.
     */
    public static final IMKTypes SMI = new IMKTypes((byte) 2);
    /**
     * The constant MPK.
     */
    public static final IMKTypes SMC = new IMKTypes((byte) 3);
    /**
     * The constant KIS.
     */
    public static final IMKTypes IDN = new IMKTypes((byte) 4);

    private static HashMap<Byte, String> toStringMap = new HashMap<>();

    static {
        toStringMap.put(AC.getValue(), "AC");
        toStringMap.put(SMI.getValue(), "SMI");
        toStringMap.put(SMC.getValue(), "SMC");
        toStringMap.put(IDN.getValue(), "IDN");
    }

    /**
     * The Value.
     */
//    , (1), (2), KIS(3), KIR(4), KTM(5), CSCK(6), PVK(7), KPVV(8), CVK(
//            9),  ZKA_KGK(16), ZKA_KKBLZ(17), ZKA_MK(18), BDK(24), IMKAC(30), IMKSMI(
//            31), IMKSMC(32), IMKDAC(33), IMKDN(34), KTK(35), PTK(36), KMC(37), IMK_CVC(
//            38),;
    byte value;

    /**
     * Instantiates a new Key types.
     */
    public IMKTypes() {

    }

    private IMKTypes(byte value) {
        this.value = value;
    }

    /**
     * From value key types.
     *
     * @param value the value
     * @return the key types
     */
    public static final IMKTypes fromValue(byte value) {
        return new IMKTypes(value);
    }

    /**
     * From value key types.
     *
     * @param value the value
     * @return the key types
     */
    public static final IMKTypes fromValue(String value) {
        for (int k : toStringMap.keySet()) {
            if (toStringMap.get(k).equals(value))
                return fromValue((byte) k);
        }
        throw new RuntimeException("can not found key type from string value" + value);
    }

    @Override
    public String toString() {
        if (toStringMap.containsKey(value))
            return toStringMap.get(value);
        return "KEY" + value;
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
        return this.getValue() == ((IMKTypes) obj).getValue();
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
