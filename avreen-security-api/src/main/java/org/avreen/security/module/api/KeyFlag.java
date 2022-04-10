package org.avreen.security.module.api;

/*
        Key Flags Indicates the session keys to generate. The function response will
        contain one or more sets of encrypted key fields as shown: one set for
        each bit set in the flags. The bit positions are allocated as follows:
        bit session key type
        0 Single-length Data Key (DPK).
        1 Single-length PIN encrypting key (PPK).
        2 Single-length MAC key (MPK).
        3 Reserved. Must be zero.
        4 Triple-length Data Key (DPK).
        5 Triple-length PIN encrypting key (PPK).
        6 Triple-length MAC key (MPK).
        7 Reserved. Must be zero.
        8 Double-length Data Key (DPK).
        9 Double-length PIN encrypting key (PPK).
        10 Double-length MAC key (MPK).
        11 Reserved. Must be zero.
        12 -13 Encryption mode for response eKSn(KSn+1)
        00 = ECB, 01 – CBC and 10 =TR-31, bit 12 is the
        least significant (right most) bit
        14-15 Reserved. Must be zero.
 */

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Key flag.
 */
public class KeyFlag implements IByteArrayConverter {
    private KeyTypes keyType;
    private KeyLength keyLength;
    private CipherMode cipherMode;

    /**
     * Gets key type.
     *
     * @return the key type
     */
    public KeyTypes getKeyType() {
        return keyType;
    }

    /**
     * Sets key type.
     *
     * @param keyType the key type
     */
    public void setKeyType(KeyTypes keyType) {
        this.keyType = keyType;
    }

    /**
     * Gets key length.
     *
     * @return the key length
     */
    public KeyLength getKeyLength() {
        return keyLength;
    }

    /**
     * Sets key length.
     *
     * @param keyLength the key length
     */
    public void setKeyLength(KeyLength keyLength) {
        this.keyLength = keyLength;
    }

    /**
     * Gets cipher mode.
     *
     * @return the cipher mode
     */
    public CipherMode getCipherMode() {
        return cipherMode;
    }

    /**
     * Sets cipher mode.
     *
     * @param cipherMode the cipher mode
     */
    public void setCipherMode(CipherMode cipherMode) {
        this.cipherMode = cipherMode;
    }

    @Override
    public byte[] encode() {
        return SecurityUtil.hex2byte("0001");
    }

    @Override
    public void decode(byte[] encoded) {

    }
}
