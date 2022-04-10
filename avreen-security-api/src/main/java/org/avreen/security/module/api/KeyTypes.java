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
public class KeyTypes {
    /**
     * The constant DPK.
     */
    public static final KeyTypes DPK = new KeyTypes(0);
    /**
     * The constant PPK.
     */
    public static final KeyTypes PPK = new KeyTypes(1);
    /**
     * The constant MPK.
     */
    public static final KeyTypes MPK = new KeyTypes(2);
    /**
     * The constant KIS.
     */
    public static final KeyTypes KIS = new KeyTypes(3);
    /**
     * The constant KIR.
     */
    public static final KeyTypes KIR = new KeyTypes(4);
    /**
     * The constant KTM.
     */
    public static final KeyTypes KTM = new KeyTypes(5);
    /**
     * The constant CSCK.
     */
    public static final KeyTypes CSCK = new KeyTypes(6);
    /**
     * The constant PVK.
     */
    public static final KeyTypes PVK = new KeyTypes(7);
    /**
     * The constant CVK.
     */
    public static final KeyTypes CVK = new KeyTypes(8);
    /**
     * The constant KPVV.
     */
    public static final KeyTypes KPVV = new KeyTypes(9);
    /**
     * The constant ZKA_KGK.
     */
    public static final KeyTypes ZKA_KGK = new KeyTypes(16);
    /**
     * The constant ZKA_KKBLZ.
     */
    public static final KeyTypes ZKA_KKBLZ = new KeyTypes(17);
    /**
     * The constant ZKA_MK.
     */
    public static final KeyTypes ZKA_MK = new KeyTypes(18);
    /**
     * The constant BDK.
     */
    public static final KeyTypes BDK = new KeyTypes(24);
    /**
     * The constant IMKAC.
     */
    public static final KeyTypes IMKAC = new KeyTypes(30);
    /**
     * The constant IMKSMI.
     */
    public static final KeyTypes IMKSMI = new KeyTypes(31);
    /**
     * The constant IMKSMC.
     */
    public static final KeyTypes IMKSMC = new KeyTypes(32);
    /**
     * The constant IMKDAC.
     */
    public static final KeyTypes IMKDAC = new KeyTypes(33);
    /**
     * The constant IMKDN.
     */
    public static final KeyTypes IMKDN = new KeyTypes(34);
    /**
     * The constant KTK.
     */
    public static final KeyTypes KTK = new KeyTypes(35);
    /**
     * The constant PTK.
     */
    public static final KeyTypes PTK = new KeyTypes(36);
    /**
     * The constant KMC.
     */
    public static final KeyTypes KMC = new KeyTypes(37);
    /**
     * The constant IMK_CVC.
     */
    public static final KeyTypes IMK_CVC = new KeyTypes(38);
    /**
     * The constant IPEK.
     */
    public static final KeyTypes IPEK = new KeyTypes(71);

    private static HashMap<Integer, String> toStringMap = new HashMap<>();

    static {
        toStringMap.put(DPK.getValue(), "DPK");
        toStringMap.put(PPK.getValue(), "PPK");
        toStringMap.put(MPK.getValue(), "MPK");
        toStringMap.put(KIS.getValue(), "KIS");
        toStringMap.put(KTM.getValue(), "KTM");
        toStringMap.put(KIR.getValue(), "KIR");
        toStringMap.put(CSCK.getValue(), "CSCK");
        toStringMap.put(PVK.getValue(), "PVK");
        toStringMap.put(CVK.getValue(), "CVK");
        toStringMap.put(KPVV.getValue(), "KPVV");
        toStringMap.put(ZKA_KGK.getValue(), "ZKA_KGK");
        toStringMap.put(ZKA_KKBLZ.getValue(), "ZKA_KKBLZ");
        toStringMap.put(ZKA_MK.getValue(), "ZKA_MK");
        toStringMap.put(BDK.getValue(), "BDK");
        toStringMap.put(IMKAC.getValue(), "IMKAC");
        toStringMap.put(IMKSMI.getValue(), "IMKSMI");
        toStringMap.put(IMKSMC.getValue(), "IMKSMC");
        toStringMap.put(IMKDAC.getValue(), "IMKDAC");
        toStringMap.put(IMKDN.getValue(), "IMKDN");
        toStringMap.put(KTK.getValue(), "KTK");
        toStringMap.put(KMC.getValue(), "KMC");
        toStringMap.put(IMK_CVC.getValue(), "IMK_CVC");
        toStringMap.put(IPEK.getValue(), "IPEK");
    }

    /**
     * The Value.
     */
//    , (1), (2), KIS(3), KIR(4), KTM(5), CSCK(6), PVK(7), KPVV(8), CVK(
//            9),  ZKA_KGK(16), ZKA_KKBLZ(17), ZKA_MK(18), BDK(24), IMKAC(30), IMKSMI(
//            31), IMKSMC(32), IMKDAC(33), IMKDN(34), KTK(35), PTK(36), KMC(37), IMK_CVC(
//            38),;
    int value;

    /**
     * Instantiates a new Key types.
     */
    public KeyTypes() {

    }

    private KeyTypes(int value) {
        this.value = value;
    }

    /**
     * From value key types.
     *
     * @param value the value
     * @return the key types
     */
    public static final KeyTypes fromValue(int value) {
        return new KeyTypes(value);
    }

    /**
     * From value key types.
     *
     * @param value the value
     * @return the key types
     */
    public static final KeyTypes fromValue(String value) {
        for (int k : toStringMap.keySet()) {
            if (toStringMap.get(k).equals(value))
                return fromValue(k);
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
    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getValue() == ((KeyTypes) obj).getValue();
    }
}
