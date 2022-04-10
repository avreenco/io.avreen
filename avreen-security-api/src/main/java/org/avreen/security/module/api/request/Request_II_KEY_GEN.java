package org.avreen.security.module.api.request;

import org.avreen.security.module.api.FunctionCodes;
import org.avreen.security.module.api.KeyFlag;
import org.avreen.security.module.api.RequestBase;
import org.avreen.security.module.api.SafenetFunctionCodeAnnotaion;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request ii key gen.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.II_KEY_GEN)
public class Request_II_KEY_GEN extends RequestBase {
    private String FM;
    private String KIS_Spec;
    private KeyFlag Key_Flags;

    /**
     * Gets fm.
     *
     * @return the fm
     */
    public String getFM() {
        return FM;
    }

    /**
     * Sets fm.
     *
     * @param FM the fm
     */
    public void setFM(String FM) {
        this.FM = FM;
    }

    /**
     * Gets kis spec.
     *
     * @return the kis spec
     */
    public String getKIS_Spec() {
        return KIS_Spec;
    }

    /**
     * Sets kis spec.
     *
     * @param KIS_Spec the kis spec
     */
    public void setKIS_Spec(String KIS_Spec) {
        this.KIS_Spec = KIS_Spec;
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
