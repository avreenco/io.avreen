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
 * The type Request emv verify ac visa.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.EMV_VERIFY_AC_VISA)
public class Request_EMV_VERIFY_AC_VISA extends RequestBase {
    private String FM;
    private String IMKAC_Spec;
    private EMVPANData PAN_Data;
    private byte[] AC_Shortened_AC;
    private byte[] AC_Data;

    /**
     * Instantiates a new Request emv verify ac visa.
     */
    public Request_EMV_VERIFY_AC_VISA() {
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
     * Get ac shortened ac byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getAC_Shortened_AC() {
        return AC_Shortened_AC;
    }

    /**
     * Sets ac shortened ac.
     *
     * @param AC_Shortened_AC the ac shortened ac
     */
    public void setAC_Shortened_AC(byte[] AC_Shortened_AC) {
        this.AC_Shortened_AC = AC_Shortened_AC;
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
