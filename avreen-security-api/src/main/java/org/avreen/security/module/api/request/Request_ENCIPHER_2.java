package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request encipher 2.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.ENCIPHER_2)
public class Request_ENCIPHER_2 extends RequestBase {
    @SafenetItemAnnotaion(length = 1, order = 1, type = SafenetFieldType.h)
    private byte FM;
    @SafenetItemAnnotaion(order = 2, type = SafenetFieldType.K_Spec)
    private String DPK_Spec;
    @SafenetItemAnnotaion(length = 3, order = 3, type = SafenetFieldType.h)
    private CipherMode CM = CipherMode.ECB;
    private EncryptionStandard encryptionStandard = EncryptionStandard.DES;
    @SafenetItemAnnotaion(length = 8, order = 4, type = SafenetFieldType.h)
    private byte[] ICV;
    @SafenetItemAnnotaion(order = 5, type = SafenetFieldType.h)
    private byte[] Data;

    /**
     * Instantiates a new Request encipher 2.
     */
    public Request_ENCIPHER_2() {
        super();
    }

    /**
     * Gets encryption standard.
     *
     * @return the encryption standard
     */
    public EncryptionStandard getEncryptionStandard() {
        return encryptionStandard;
    }

    /**
     * Sets encryption standard.
     *
     * @param encryptionStandard the encryption standard
     */
    public void setEncryptionStandard(EncryptionStandard encryptionStandard) {
        this.encryptionStandard = encryptionStandard;
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
     * @param dPK_Spec the d pk spec
     */
    public void setDPK_Spec(String dPK_Spec) {
        DPK_Spec = dPK_Spec;
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
