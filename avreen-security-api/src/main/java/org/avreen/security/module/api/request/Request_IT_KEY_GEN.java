package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request it key gen.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.IT_KEY_GEN)
public class Request_IT_KEY_GEN extends RequestBase {
    @SafenetItemAnnotaion(length = 1, order = 1, type = SafenetFieldType.h)
    private byte FM;
    @SafenetItemAnnotaion(order = 2, type = SafenetFieldType.K_Spec)
    private String KTM_Spec;
    @SafenetItemAnnotaion(order = 2, length = 2, type = SafenetFieldType.h)
    private KeyFlag Key_Flags;


    /**
     * Instantiates a new Request it key gen.
     */
    public Request_IT_KEY_GEN() {
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
     * Gets ktm spec.
     *
     * @return the ktm spec
     */
    public String getKTM_Spec() {
        return KTM_Spec;
    }

    /**
     * Sets ktm spec.
     *
     * @param kTM_Spec the k tm spec
     */
    public void setKTM_Spec(String kTM_Spec) {
        KTM_Spec = kTM_Spec;
    }

    /**
     * Gets key flags.
     *
     * @return the key flags
     */
    public KeyFlag getKey_Flags() {
        return Key_Flags;
    }

    /**
     * Sets key flags.
     *
     * @param key_Flags the key flags
     */
    public void setKey_Flags(KeyFlag key_Flags) {
        Key_Flags = key_Flags;
    }


}
