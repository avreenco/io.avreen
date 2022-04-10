package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request decipher 4.
 */
//Page 219
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.DECIPHER_4)
public class Request_DECIPHER_4 extends RequestBase {
    @SafenetItemAnnotaion(length = 1, order = 1, type = SafenetFieldType.h)
    private byte FM = 0;
    @SafenetItemAnnotaion(order = 1, type = SafenetFieldType.K_Spec)
    // Format 20
    private String DPK_Spec;
    @SafenetItemAnnotaion(order = 2, length = 1, type = SafenetFieldType.h)
    private CipherMode CM;
    @SafenetItemAnnotaion(order = 3, length = 8, type = SafenetFieldType.h)
    private byte[] ICV;
    @SafenetItemAnnotaion(order = 4, type = SafenetFieldType.h)
    private byte[] eDPK_Data;

    /**
     * Instantiates a new Request decipher 4.
     */
    public Request_DECIPHER_4() {
        super();
    }

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
     * @param fM the f m
     */
    public void setFM(byte fM) {
        FM = fM;
    }

    /**
     * Gets cm.
     *
     * @return the cm
     */
    public CipherMode getCM() {
        return CM;
    }

    /**
     * Sets cm.
     *
     * @param cM the c m
     */
    public void setCM(CipherMode cM) {
        CM = cM;
    }

    /**
     * Get icv byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getICV() {
        return ICV;
    }

    /**
     * Sets icv.
     *
     * @param iCV the cv
     */
    public void setICV(byte[] iCV) {
        ICV = iCV;
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

    /**
     * Gets dpk spec.
     *
     * @return the dpk spec
     */
    public String getDPK_Spec() {
        return DPK_Spec;
    }

    /**
     * Sets dpk spec.
     *
     * @param DPK_Spec the dpk spec
     */
    public void setDPK_Spec(String DPK_Spec) {
        this.DPK_Spec = DPK_Spec;
    }
}
