package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request mac ver final.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.MAC_VER_FINAL)
public class Request_MAC_VER_FINAL extends RequestBase {
    @SafenetItemAnnotaion(length = 1, order = 1, type = SafenetFieldType.h)
    private byte FM = 0;
    @SafenetItemAnnotaion(length = 1, order = 2, type = SafenetFieldType.h)
    private MACAlgorithm Alg;
    @SafenetItemAnnotaion(length = 1, order = 3, type = SafenetFieldType.h)
    private byte[] ICD;
    @SafenetItemAnnotaion(order = 5, type = SafenetFieldType.K_Spec)
    private String MPK_Spec;
    @SafenetItemAnnotaion(order = 6, type = SafenetFieldType.h)
    private byte[] MAC;
    @SafenetItemAnnotaion(order = 7, type = SafenetFieldType.h)
    private byte[] Data;

    /**
     * Instantiates a new Request mac ver final.
     */
    public Request_MAC_VER_FINAL() {
        super();
    }

    /**
     * Get mac byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getMAC() {
        return MAC;
    }

    /**
     * Sets mac.
     *
     * @param MAC the mac
     */
    public void setMAC(byte[] MAC) {
        this.MAC = MAC;
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
     * Gets alg.
     *
     * @return the alg
     */
    public MACAlgorithm getAlg() {
        return Alg;
    }

    /**
     * Sets alg.
     *
     * @param alg the alg
     */
    public void setAlg(MACAlgorithm alg) {
        Alg = alg;
    }

    /**
     * Get icd byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getICD() {
        return ICD;
    }

    /**
     * Sets icd.
     *
     * @param iCD the cd
     */
    public void setICD(byte[] iCD) {
        ICD = iCD;
    }

    /**
     * Gets mpk spec.
     *
     * @return the mpk spec
     */
    public String getMPK_Spec() {
        return MPK_Spec;
    }

    /**
     * Sets mpk spec.
     *
     * @param mPK_Spec the m pk spec
     */
    public void setMPK_Spec(String mPK_Spec) {
        MPK_Spec = mPK_Spec;
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
