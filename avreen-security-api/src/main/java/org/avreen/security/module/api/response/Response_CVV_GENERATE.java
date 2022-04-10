package org.avreen.security.module.api.response;

import org.avreen.security.module.api.ResponseBase;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response cvv generate.
 */
public class Response_CVV_GENERATE extends ResponseBase {


    private byte[] CVV;

    /**
     * Get cvv byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getCVV() {
        return CVV;
    }

    /**
     * Sets cvv.
     *
     * @param CVV the cvv
     */
    public void setCVV(byte[] CVV) {
        this.CVV = CVV;
    }
}
