package org.avreen.security.module.api.request;

import org.avreen.security.module.api.EMVPANData;
import org.avreen.security.module.api.FunctionCodes;
import org.avreen.security.module.api.RequestBase;
import org.avreen.security.module.api.SafenetFunctionCodeAnnotaion;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request emv verify ac emv 2000.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.EMV_VERIFY_AC_EMV2000)
public class Request_EMV_VERIFY_AC_EMV2000 extends RequestBase {
    private String FM;
    private String IMKAC_Spec;
    private EMVPANData PAN_Data;
    private byte[] IV;
    private byte h;
    private byte B;
    private byte[] ATC;
    private byte[] AC_CAP_Token;
    private byte[] AC_Data;

    /**
     * Instantiates a new Request emv verify ac emv 2000.
     */
    public Request_EMV_VERIFY_AC_EMV2000() {
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
     * Gets imkac spec.
     *
     * @return the imkac spec
     */
    public String getIMKAC_Spec() {
        return IMKAC_Spec;
    }

    /**
     * Sets imkac spec.
     *
     * @param IMKAC_Spec the imkac spec
     */
    public void setIMKAC_Spec(String IMKAC_Spec) {
        this.IMKAC_Spec = IMKAC_Spec;
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
     * Get ac cap token byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getAC_CAP_Token() {
        return AC_CAP_Token;
    }

    /**
     * Sets ac cap token.
     *
     * @param AC_CAP_Token the ac cap token
     */
    public void setAC_CAP_Token(byte[] AC_CAP_Token) {
        this.AC_CAP_Token = AC_CAP_Token;
    }

    /**
     * Get ac data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getAC_Data() {
        return AC_Data;
    }

    /**
     * Sets ac data.
     *
     * @param AC_Data the ac data
     */
    public void setAC_Data(byte[] AC_Data) {
        this.AC_Data = AC_Data;
    }
}
