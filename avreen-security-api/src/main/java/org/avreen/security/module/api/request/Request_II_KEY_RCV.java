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
 * The type Request ii key rcv.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.II_KEY_RCV)
public class Request_II_KEY_RCV extends RequestBase {
    private String FM;
    private String KIR_Spec;
    private KeyFlag Key_Flags;
    private byte[] eKIRn_KB;

    /**
     * Instantiates a new Request ii key rcv.
     */
    public Request_II_KEY_RCV() {
        super();
    }

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
     * @param fM the f m
     */
    public void setFM(String fM) {
        FM = fM;
    }

    /**
     * Gets kir spec.
     *
     * @return the kir spec
     */
    public String getKIR_Spec() {
        return KIR_Spec;
    }

    /**
     * Sets kir spec.
     *
     * @param kIR_Spec the k ir spec
     */
    public void setKIR_Spec(String kIR_Spec) {
        KIR_Spec = kIR_Spec;
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

    /**
     * Gete ki rn kb byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] geteKIRn_KB() {
        return eKIRn_KB;
    }

    /**
     * Sets ki rn kb.
     *
     * @param eKIRn_KB the e ki rn kb
     */
    public void seteKIRn_KB(byte[] eKIRn_KB) {
        this.eKIRn_KB = eKIRn_KB;
    }

}
