package org.avreen.security.module.api.response;

import org.avreen.security.module.api.ResponseBase;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response ii key gen.
 */
public class Response_II_KEY_GEN extends ResponseBase {
    private String KS_Spec;
    private byte[] eKIS_KB;
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
     * @param KS_Spec the ks spec
     */
    public void setKS_Spec(String KS_Spec) {
        this.KS_Spec = KS_Spec;
    }

    /**
     * Gete kis kb byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] geteKIS_KB() {
        return eKIS_KB;
    }

    /**
     * Sets kis kb.
     *
     * @param eKIS_KB the e kis kb
     */
    public void seteKIS_KB(byte[] eKIS_KB) {
        this.eKIS_KB = eKIS_KB;
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
     * @param KVC the kvc
     */
    public void setKVC(byte[] KVC) {
        this.KVC = KVC;
    }
}
