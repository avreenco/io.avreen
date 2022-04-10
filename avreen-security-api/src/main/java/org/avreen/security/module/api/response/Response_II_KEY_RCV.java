package org.avreen.security.module.api.response;

import org.avreen.security.module.api.ResponseBase;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response ii key rcv.
 */
public class Response_II_KEY_RCV extends ResponseBase {
    private String KS_Spec;
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
