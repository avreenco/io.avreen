package org.avreen.security.module.api.response;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response encipher 2.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.ENCIPHER_2)
public class Response_ENCIPHER_2 extends ResponseBase {
    @SafenetItemAnnotaion(order = 1, length = 8, type = SafenetFieldType.h)
    private byte[] OCV;
    @SafenetItemAnnotaion(order = 2, type = SafenetFieldType.h)
    private byte[] eDPK_Data;

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
     * Gete dpk data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] geteDPK_Data() {
        return eDPK_Data;
    }

    /**
     * Sets dpk data.
     *
     * @param eDPK_Data the e dpk data
     */
    public void seteDPK_Data(byte[] eDPK_Data) {
        this.eDPK_Data = eDPK_Data;
    }

}
