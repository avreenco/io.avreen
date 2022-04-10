package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request emv generate arpc.
 */
//Page 268
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.EMV_GENERATE_ARPC)
public class Request_EMV_GENERATE_ARPC extends RequestBase {
    @SafenetItemAnnotaion(order = 1, length = 1, type = SafenetFieldType.h)
    private byte FM = 0;
    @SafenetItemAnnotaion(order = 2, length = -1, type = SafenetFieldType.K_Spec)
    private String IMKAC_Spec;
    @SafenetItemAnnotaion(order = 3, length = 8, type = SafenetFieldType.h)
    private EMVPANData PAN_Data;
    @SafenetItemAnnotaion(order = 4, length = 16, type = SafenetFieldType.h)
    private byte[] IV;
    @SafenetItemAnnotaion(order = 5, length = 1, type = SafenetFieldType.h)
    private byte h;
    @SafenetItemAnnotaion(order = 6, length = 1, type = SafenetFieldType.h)
    private byte B;
    @SafenetItemAnnotaion(order = 7, length = 2, type = SafenetFieldType.h)
    private byte[] ATC;
    @SafenetItemAnnotaion(order = 8, length = 8, type = SafenetFieldType.h)
    private byte[] ARPC_Data;

    /**
     * Instantiates a new Request emv generate arpc.
     */
    public Request_EMV_GENERATE_ARPC() {
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
     * Get arpc data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getARPC_Data() {
        return ARPC_Data;
    }

    /**
     * Sets arpc data.
     *
     * @param ARPC_Data the arpc data
     */
    public void setARPC_Data(byte[] ARPC_Data) {
        this.ARPC_Data = ARPC_Data;
    }
}
