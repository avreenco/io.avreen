package org.avreen.security.module.api;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Key detail.
 */
public class KeyDetail {
    private KeyTypes keyType;
    private KeyLength keyLength;

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
}
