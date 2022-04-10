package org.avreen.security.module.api.response;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response advance random key gen.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.ADVANCE_RANDOMKEY_GEN)
public class Response_ADVANCE_RANDOM_KEY_GEN extends ResponseBase {
    @SafenetItemAnnotaion(order = 1, type = SafenetFieldType.K_Spec)
    private String eKM_Key;

    /**
     * Gets km key.
     *
     * @return the km key
     */
    public String geteKM_Key() {
        return eKM_Key;
    }

    /**
     * Sets km key.
     *
     * @param eKM_Key the e km key
     */
    public void seteKM_Key(String eKM_Key) {
        this.eKM_Key = eKM_Key;
    }
}
