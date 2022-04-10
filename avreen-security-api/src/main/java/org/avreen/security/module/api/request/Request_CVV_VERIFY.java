package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request cvv verify.
 */
/*
    Page 196
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.CVV_VERIFY)
public class Request_CVV_VERIFY extends RequestBase {
    @SafenetItemAnnotaion(length = 1, order = 1, type = SafenetFieldType.h)
    private byte FM = 0;
    @SafenetItemAnnotaion(order = 2, type = SafenetFieldType.K_Spec)
    private String CVK_Spec;
    @SafenetItemAnnotaion(length = 16, order = 3, type = SafenetFieldType.h)
    private byte[] CVV_DATE;
    @SafenetItemAnnotaion(length = 2, order = 4, type = SafenetFieldType.h)
    private byte[] CVV;

    /**
     * Instantiates a new Request cvv verify.
     */
    public Request_CVV_VERIFY() {
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
     * Gets cvk spec.
     *
     * @return the cvk spec
     */
    public String getCVK_Spec() {
        return CVK_Spec;
    }

    /**
     * Sets cvk spec.
     *
     * @param CVK_Spec the cvk spec
     */
    public void setCVK_Spec(String CVK_Spec) {
        this.CVK_Spec = CVK_Spec;
    }

    /**
     * Get cvv byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getCVV() {
        return CVV;
    }

    /**
     * Sets cvv.
     *
     * @param CVV the cvv
     */
    public void setCVV(byte[] CVV) {
        this.CVV = CVV;
    }

    /**
     * Get cvv date byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getCVV_DATE() {
        return CVV_DATE;
    }

    /**
     * Sets cvv date.
     *
     * @param CVV_DATE the cvv date
     */
    public void setCVV_DATE(byte[] CVV_DATE) {
        this.CVV_DATE = CVV_DATE;
    }
}
