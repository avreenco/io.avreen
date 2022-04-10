package org.avreen.security.module.api.response;

import org.avreen.security.module.api.ResponseBase;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response dukpt ik derive 2.
 */
public class Response_DUKPT_IK_Derive_2 extends ResponseBase {
    private byte[] eKTM_IK;
    private byte[] KVC;

    /**
     * Gete ktm ik byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] geteKTM_IK() {
        return eKTM_IK;
    }

    /**
     * Sets ktm ik.
     *
     * @param eKTM_IK the e ktm ik
     */
    public void seteKTM_IK(byte[] eKTM_IK) {
        this.eKTM_IK = eKTM_IK;
    }

    /**
     * Get kvc byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getKVC() {
        return KVC;
    }

    /**
     * Sets kvc.
     *
     * @param kVC the k vc
     */
    public void setKVC(byte[] kVC) {
        KVC = kVC;
    }

}
