package org.avreen.security.module.api.response;

import org.avreen.security.module.api.ResponseBase;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response decipher 4.
 */
public class Response_DECIPHER_4 extends ResponseBase {
    private byte[] OCV;
    private byte[] Data;

    /**
     * Get ocv byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getOCV() {
        return OCV;
    }

    /**
     * Sets ocv.
     *
     * @param oCV the o cv
     */
    public void setOCV(byte[] oCV) {
        OCV = oCV;
    }

    /**
     * Get data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getData() {
        return Data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(byte[] data) {
        Data = data;
    }

}
