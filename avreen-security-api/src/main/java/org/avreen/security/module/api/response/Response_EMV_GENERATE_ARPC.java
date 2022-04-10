package org.avreen.security.module.api.response;

import org.avreen.security.module.api.ResponseBase;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response emv generate arpc.
 */
public class Response_EMV_GENERATE_ARPC extends ResponseBase {
    private byte[] ARPC;

    /**
     * Get arpc byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getARPC() {
        return ARPC;
    }

    /**
     * Sets arpc.
     *
     * @param ARPC the arpc
     */
    public void setARPC(byte[] ARPC) {
        this.ARPC = ARPC;
    }
}
