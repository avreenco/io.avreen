package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request pin off.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.PIN_OFF)
public class Request_PIN_OFF extends RequestBase {
    private String FM;
    private byte[] ePPK_PIN;
    private String PPK_Spec;
    private PinFormat PF;
    private byte[] ANB;
    private String PVK_Spec;
    private byte[] Validation_Data;
    private EncryptionStandard encryptionStandard = EncryptionStandard.DES;

    /**
     * Instantiates a new Request pin off.
     */
    public Request_PIN_OFF() {
        super();
    }

    /**
     * Gets encryption standard.
     *
     * @return the encryption standard
     */
    public EncryptionStandard getEncryptionStandard() {
        return encryptionStandard;
    }

    /**
     * Sets encryption standard.
     *
     * @param encryptionStandard the encryption standard
     */
    public void setEncryptionStandard(EncryptionStandard encryptionStandard) {
        this.encryptionStandard = encryptionStandard;
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
     * Gete ppk pin byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getePPK_PIN() {
        return ePPK_PIN;
    }

    /**
     * Sets ppk pin.
     *
     * @param ePPK_PIN the e ppk pin
     */
    public void setePPK_PIN(byte[] ePPK_PIN) {
        this.ePPK_PIN = ePPK_PIN;
    }

    /**
     * Gets ppk spec.
     *
     * @return the ppk spec
     */
    public String getPPK_Spec() {
        return PPK_Spec;
    }

    /**
     * Sets ppk spec.
     *
     * @param pPK_Spec the p pk spec
     */
    public void setPPK_Spec(String pPK_Spec) {
        PPK_Spec = pPK_Spec;
    }

    /**
     * Gets pf.
     *
     * @return the pf
     */
    public PinFormat getPF() {
        return PF;
    }

    /**
     * Sets pf.
     *
     * @param pF the p f
     */
    public void setPF(PinFormat pF) {
        PF = pF;
    }

    /**
     * Get anb byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getANB() {
        return ANB;
    }

    /**
     * Sets anb.
     *
     * @param aNB the a nb
     */
    public void setANB(byte[] aNB) {
        ANB = aNB;
    }

    /**
     * Gets pvk spec.
     *
     * @return the pvk spec
     */
    public String getPVK_Spec() {
        return PVK_Spec;
    }

    /**
     * Sets pvk spec.
     *
     * @param pVK_Spec the p vk spec
     */
    public void setPVK_Spec(String pVK_Spec) {
        PVK_Spec = pVK_Spec;
    }

    /**
     * Get validation data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getValidation_Data() {
        return Validation_Data;
    }

    /**
     * Sets validation data.
     *
     * @param validation_Data the validation data
     */
    public void setValidation_Data(byte[] validation_Data) {
        Validation_Data = validation_Data;
    }


}
