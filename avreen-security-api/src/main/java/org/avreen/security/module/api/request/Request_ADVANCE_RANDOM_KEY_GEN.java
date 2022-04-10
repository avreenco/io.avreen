package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request advance random key gen.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.ADVANCE_RANDOMKEY_GEN)
public class Request_ADVANCE_RANDOM_KEY_GEN extends RequestBase {
    @SafenetItemAnnotaion(length = 1, order = 1, type = SafenetFieldType.h)
    private byte FM;
    @SafenetItemAnnotaion(length = 1, order = 2, type = SafenetFieldType.h)
    private byte KeyFormat;
    @SafenetItemAnnotaion(length = -1, order = 3, type = SafenetFieldType.h)
    private KeyDetail KeyDetail;

    /**
     * Gets fm.
     *
     * @return the fm
     */
    public byte getFM() {
        return FM;
    }

    /**
     * Sets fm.
     *
     * @param FM the fm
     */
    public void setFM(byte FM) {
        this.FM = FM;
    }

    /**
     * Gets key format.
     *
     * @return the key format
     */
    public byte getKeyFormat() {
        return KeyFormat;
    }

    /**
     * Sets key format.
     *
     * @param keyFormat the key format
     */
    public void setKeyFormat(byte keyFormat) {
        KeyFormat = keyFormat;
    }

    /**
     * Gets key detail.
     *
     * @return the key detail
     */
    public KeyDetail getKeyDetail() {
        return KeyDetail;
    }

    /**
     * Sets key detail.
     *
     * @param keyDetail the key detail
     */
    public void setKeyDetail(KeyDetail keyDetail) {
        KeyDetail = keyDetail;
    }
}
