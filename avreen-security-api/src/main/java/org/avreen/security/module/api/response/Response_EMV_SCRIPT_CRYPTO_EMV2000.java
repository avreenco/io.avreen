package org.avreen.security.module.api.response;

import org.avreen.security.module.api.ResponseBase;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response emv script crypto emv 2000.
 */
public class Response_EMV_SCRIPT_CRYPTO_EMV2000 extends ResponseBase {
    private byte[] eSKSMC_Text;
    private byte[] MAC;

    /**
     * Gete sksmc text byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] geteSKSMC_Text() {
        return eSKSMC_Text;
    }

    /**
     * Sets sksmc text.
     *
     * @param eSKSMC_Text the e sksmc text
     */
    public void seteSKSMC_Text(byte[] eSKSMC_Text) {
        this.eSKSMC_Text = eSKSMC_Text;
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
}
