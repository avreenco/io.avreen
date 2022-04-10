package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request emv script crypto emv 2000.
 */
//Page 270
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.EMV_SCRIPT_CRYPTO_EMV2000)
public class Request_EMV_SCRIPT_CRYPTO_EMV2000 extends RequestBase {

    @SafenetItemAnnotaion(order = 1, length = 1, type = SafenetFieldType.h)
    private byte FM = 0;
    @SafenetItemAnnotaion(order = 2, length = 1, type = SafenetFieldType.h)
    private SelectCode SC;
    @SafenetItemAnnotaion(order = 3, length = -1, type = SafenetFieldType.K_Spec)
    private String IMKSMI_Spec;
    @SafenetItemAnnotaion(order = 4, length = -1, type = SafenetFieldType.K_Spec)
    private String IMKSMC_Spec;
    @SafenetItemAnnotaion(order = 5, length = 8, type = SafenetFieldType.h)
    private EMVPANData PAN_Data;
    @SafenetItemAnnotaion(order = 6, length = 16, type = SafenetFieldType.h)
    private byte[] IV;
    @SafenetItemAnnotaion(order = 7, length = 1, type = SafenetFieldType.h)
    private byte h;
    @SafenetItemAnnotaion(order = 8, length = 1, type = SafenetFieldType.h)
    private byte B;
    @SafenetItemAnnotaion(order = 9, length = 2, type = SafenetFieldType.h)
    private byte[] ATC;
    @SafenetItemAnnotaion(order = 10, length = 1, type = SafenetFieldType.h)
    private CipherMode Mode;
    @SafenetItemAnnotaion(order = 11, length = -1, type = SafenetFieldType.h)
    private byte[] Text;
    @SafenetItemAnnotaion(order = 12, length = 2, type = SafenetFieldType.h)
    private byte[] Offset;
    @SafenetItemAnnotaion(order = 13, length = -1, type = SafenetFieldType.h)
    private byte[] Script_Data;

    /**
     * Instantiates a new Request emv script crypto emv 2000.
     */
    public Request_EMV_SCRIPT_CRYPTO_EMV2000() {
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
     * Gets sc.
     *
     * @return the sc
     */
    public SelectCode getSC() {
        return SC;
    }

    /**
     * Sets sc.
     *
     * @param SC the sc
     */
    public void setSC(SelectCode SC) {
        this.SC = SC;
    }

    /**
     * Gets imksmi spec.
     *
     * @return the imksmi spec
     */
    public String getIMKSMI_Spec() {
        return IMKSMI_Spec;
    }

    /**
     * Sets imksmi spec.
     *
     * @param IMKSMI_Spec the imksmi spec
     */
    public void setIMKSMI_Spec(String IMKSMI_Spec) {
        this.IMKSMI_Spec = IMKSMI_Spec;
    }

    /**
     * Gets imksmc spec.
     *
     * @return the imksmc spec
     */
    public String getIMKSMC_Spec() {
        return IMKSMC_Spec;
    }

    /**
     * Sets imksmc spec.
     *
     * @param IMKSMC_Spec the imksmc spec
     */
    public void setIMKSMC_Spec(String IMKSMC_Spec) {
        this.IMKSMC_Spec = IMKSMC_Spec;
    }

    /**
     * Gets pan data.
     *
     * @return the pan data
     */
    public EMVPANData getPAN_Data() {
        return PAN_Data;
    }

    /**
     * Sets pan data.
     *
     * @param PAN_Data the pan data
     */
    public void setPAN_Data(EMVPANData PAN_Data) {
        this.PAN_Data = PAN_Data;
    }

    /**
     * Get iv byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getIV() {
        return IV;
    }

    /**
     * Sets iv.
     *
     * @param IV the iv
     */
    public void setIV(byte[] IV) {
        this.IV = IV;
    }

    /**
     * Gets h.
     *
     * @return the h
     */
    public byte getH() {
        return h;
    }

    /**
     * Sets h.
     *
     * @param h the h
     */
    public void setH(byte h) {
        this.h = h;
    }

    /**
     * Gets b.
     *
     * @return the b
     */
    public byte getB() {
        return B;
    }

    /**
     * Sets b.
     *
     * @param b the b
     */
    public void setB(byte b) {
        B = b;
    }

    /**
     * Get atc byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getATC() {
        return ATC;
    }

    /**
     * Sets atc.
     *
     * @param ATC the atc
     */
    public void setATC(byte[] ATC) {
        this.ATC = ATC;
    }

    /**
     * Gets mode.
     *
     * @return the mode
     */
    public CipherMode getMode() {
        return Mode;
    }

    /**
     * Sets mode.
     *
     * @param mode the mode
     */
    public void setMode(CipherMode mode) {
        Mode = mode;
    }

    /**
     * Get text byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getText() {
        return Text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(byte[] text) {
        Text = text;
    }

    /**
     * Get offset byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getOffset() {
        return Offset;
    }

    /**
     * Sets offset.
     *
     * @param offset the offset
     */
    public void setOffset(byte[] offset) {
        Offset = offset;
    }

    /**
     * Get script data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getScript_Data() {
        return Script_Data;
    }

    /**
     * Sets script data.
     *
     * @param script_Data the script data
     */
    public void setScript_Data(byte[] script_Data) {
        Script_Data = script_Data;
    }
}
