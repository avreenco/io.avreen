package org.avreen.security.module.api.response;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response mac gen final.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.MAC_GEN_FINAL)
public class Response_MAC_GEN_FINAL extends ResponseBase {
    @SafenetItemAnnotaion(order = 1, type = SafenetFieldType.h)
    private byte[] MAC;

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
     * @param mAC the m ac
     */
    public void setMAC(byte[] mAC) {
        MAC = mAC;
    }

}
