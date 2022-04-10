package org.avreen.security.module.api.response;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response it key gen.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.IT_KEY_GEN)
public class Response_IT_KEY_GEN extends ResponseBase {
    //01088F7319767FEAE49109108907C5112DE5A869AA4CB3
    @SafenetItemAnnotaion(order = 1, length = 1, type = SafenetFieldType.h)
    private byte keyCount;
    @SafenetItemAnnotaion(order = 2, type = SafenetFieldType.h)
    private byte[] eKTM_KB;
    @SafenetItemAnnotaion(order = 3, type = SafenetFieldType.K_Spec)
    private String KS_Spec;

    @SafenetItemAnnotaion(order = 4, length = 3, type = SafenetFieldType.h)
    private byte[] KVC;

    /**
     * Gets ks spec.
     *
     * @return the ks spec
     */
    public String getKS_Spec() {
        return KS_Spec;
    }

    /**
     * Sets ks spec.
     *
     * @param kS_Spec the k s spec
     */
    public void setKS_Spec(String kS_Spec) {
        KS_Spec = kS_Spec;
    }

    /**
     * Gete ktm kb byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] geteKTM_KB() {
        return eKTM_KB;
    }

    /**
     * Sets ktm kb.
     *
     * @param eKTM_KB the e ktm kb
     */
    public void seteKTM_KB(byte[] eKTM_KB) {
        this.eKTM_KB = eKTM_KB;
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

    /**
     * Gets key count.
     *
     * @return the key count
     */
    public byte getKeyCount() {
        return keyCount;
    }

    /**
     * Sets key count.
     *
     * @param keyCount the key count
     */
    public void setKeyCount(byte keyCount) {
        this.keyCount = keyCount;
    }

}
