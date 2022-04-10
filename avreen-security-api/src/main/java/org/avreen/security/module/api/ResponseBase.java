package org.avreen.security.module.api;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response base.
 */
public class ResponseBase extends SecurityFunctionMessage
{
    private String returnCode = "BB";

    /**
     * Instantiates a new Response base.
     */
    public ResponseBase() {
        super();
    }

    /**
     * Gets return code.
     *
     * @return the return code
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * Sets return code.
     *
     * @param returnCode the return code
     */
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * Is approve boolean.
     *
     * @return the boolean
     */
    public boolean isApprove() {
        return "00".equals(getReturnCode());
    }

    /**
     * Sets approve.
     */
    public void setApprove() {
        setReturnCode("00");
    }


}
