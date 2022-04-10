package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request dukpt ik derive 2.
 */
//Page 86
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.DUKPT_IK_Derive_2)
public class Request_DUKPT_IK_Derive_2 extends RequestBase {
    @SafenetItemAnnotaion(length = 1, order = 1, type = SafenetFieldType.h)
    private byte FM = 0;
    @SafenetItemAnnotaion(order = 2, type = SafenetFieldType.K_Spec)
    private String KTM;
    @SafenetItemAnnotaion(order = 3, type = SafenetFieldType.K_Spec)
    private String IK;
    @SafenetItemAnnotaion(order = 4, length = 1, type = SafenetFieldType.h)
    private CipherMode EncMode;

    /**
     * Instantiates a new Request dukpt ik derive 2.
     */
    public Request_DUKPT_IK_Derive_2() {
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
     * Gets ktm.
     *
     * @return the ktm
     */
    public String getKTM() {
        return KTM;
    }

    /**
     * Sets ktm.
     *
     * @param kTM the k tm
     */
    public void setKTM(String kTM) {
        KTM = kTM;
    }

    /**
     * Gets enc mode.
     *
     * @return the enc mode
     */
    public CipherMode getEncMode() {
        return EncMode;
    }

    /**
     * Sets enc mode.
     *
     * @param encMode the enc mode
     */
    public void setEncMode(CipherMode encMode) {
        EncMode = encMode;
    }

    /**
     * Gets ik.
     *
     * @return the ik
     */
    public String getIK() {
        return IK;
    }

    /**
     * Sets ik.
     *
     * @param IK the ik
     */
    public void setIK(String IK) {
        this.IK = IK;
    }
}
