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
 * The type Hash algorithms.
 */
public class HashAlgorithms implements IByteArrayConverter {
    /**
     * The constant SHA_1.
     */
    public static final HashAlgorithms SHA_1 = new HashAlgorithms(0);
    /**
     * The constant RIPEMD320.
     */
    public static final HashAlgorithms RIPEMD320 = new HashAlgorithms(1);
    /**
     * The constant SHA3_224.
     */
    public static final HashAlgorithms SHA3_224 = new HashAlgorithms(2);
    /**
     * The constant SHA3_256.
     */
    public static final HashAlgorithms SHA3_256 = new HashAlgorithms(3);
    /**
     * The constant SHA3_384.
     */
    public static final HashAlgorithms SHA3_384 = new HashAlgorithms(4);
    /**
     * The constant SHA3_512.
     */
    public static final HashAlgorithms SHA3_512 = new HashAlgorithms(5);
    /**
     * The constant TIGER.
     */
    public static final HashAlgorithms TIGER = new HashAlgorithms(20);
    /**
     * The constant WHIRLPOOL.
     */
    public static final HashAlgorithms WHIRLPOOL = new HashAlgorithms(21);
    /**
     * The constant RIPEMD256.
     */
    public static final HashAlgorithms RIPEMD256 = new HashAlgorithms(22);
    /**
     * The constant RIPEMD128.
     */
    public static final HashAlgorithms RIPEMD128 = new HashAlgorithms(23);
    /**
     * The constant RIPEMD160.
     */
    public static final HashAlgorithms RIPEMD160 = new HashAlgorithms(24);
    /**
     * The constant SHA_224.
     */
    public static final HashAlgorithms SHA_224 = new HashAlgorithms(25);
    /**
     * The constant SHA_256.
     */
    public static final HashAlgorithms SHA_256 = new HashAlgorithms(26);
    /**
     * The constant SHA_512_224.
     */
    public static final HashAlgorithms SHA_512_224 = new HashAlgorithms(27);
    /**
     * The constant GOST3411.
     */
    public static final HashAlgorithms GOST3411 = new HashAlgorithms(28);
    /**
     * The constant SHA_512_256.
     */
    public static final HashAlgorithms SHA_512_256 = new HashAlgorithms(29);
    /**
     * The constant SHA_384.
     */
    public static final HashAlgorithms SHA_384 = new HashAlgorithms(30);
    /**
     * The constant SM3.
     */
    public static final HashAlgorithms SM3 = new HashAlgorithms(31);
    /**
     * The constant MD5.
     */
    public static final HashAlgorithms MD5 = new HashAlgorithms(32);
    /**
     * The constant MD4.
     */
    public static final HashAlgorithms MD4 = new HashAlgorithms(33);
    /**
     * The constant MD2.
     */
    public static final HashAlgorithms MD2 = new HashAlgorithms(34);

    private static HashMap<Integer, String> toStringMap = new HashMap<>();

    static {
        toStringMap.put(GOST3411.getValue(), "GOST3411");
        toStringMap.put(MD2.getValue(), "MD2");
        toStringMap.put(MD4.getValue(), "MD4");
        toStringMap.put(MD5.getValue(), "MD5");
        toStringMap.put(SHA_1.getValue(), "SHA-1");
        toStringMap.put(RIPEMD128.getValue(), "RIPEMD128");
        toStringMap.put(RIPEMD160.getValue(), "RIPEMD160");
        toStringMap.put(RIPEMD256.getValue(), "RIPEMD256");
        toStringMap.put(RIPEMD320.getValue(), "RIPEMD320");
        toStringMap.put(SHA_224.getValue(), "SHA-224");
        toStringMap.put(SHA_256.getValue(), "SHA-256");
        toStringMap.put(SHA_384.getValue(), "SHA-384");
        toStringMap.put(SHA3_224.getValue(), "SHA-224");
        toStringMap.put(SHA3_256.getValue(), "SHA-256");
        toStringMap.put(SHA3_384.getValue(), "SHA-384");
        toStringMap.put(SHA3_512.getValue(), "SHA-512");
        toStringMap.put(SHA_512_224.getValue(), "SHA-512/224");
        toStringMap.put(SHA_512_256.getValue(), "SHA-512/256");
        toStringMap.put(SM3.getValue(), "SM3");
        toStringMap.put(TIGER.getValue(), "TIGER");
        toStringMap.put(WHIRLPOOL.getValue(), "WHIRLPOOL");
    }

    /**
     * The Value.
     */
    int value;

    /**
     * Instantiates a new Hash algorithms.
     */
    public HashAlgorithms() {

    }

    private HashAlgorithms(int value) {
        this.value = value;
    }

    /**
     * From value hash algorithms.
     *
     * @param value the value
     * @return the hash algorithms
     */
    public static final HashAlgorithms fromValue(int value) {
        return new HashAlgorithms(value);
    }

    /**
     * From value hash algorithms.
     *
     * @param value the value
     * @return the hash algorithms
     */
    public static final HashAlgorithms fromValue(String value) {
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
        return "HASH" + value;
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
        return this.getValue() == ((HashAlgorithms) obj).getValue();
    }

    @Override
    public byte[] encode() {
        return new byte[]{(byte) value};
    }

    @Override
    public void decode(byte[] encoded) {
        value = encoded[0];
    }
}
