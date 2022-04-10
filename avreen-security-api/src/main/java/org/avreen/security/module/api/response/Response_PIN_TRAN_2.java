package org.avreen.security.module.api.response;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response pin tran 2.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.PIN_TRAN_2)
public class Response_PIN_TRAN_2 extends ResponseBase {
    @SafenetItemAnnotaion(order = 1, length = 8, type = SafenetFieldType.h)
    private byte[] ePPKo_PIN;

    /**
     * Gete pp ko pin byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getePPKo_PIN() {
        return ePPKo_PIN;
    }

    /**
     * Sets pp ko pin.
     *
     * @param ePPKo_PIN the e pp ko pin
     */
    public void setePPKo_PIN(byte[] ePPKo_PIN) {
        this.ePPKo_PIN = ePPKo_PIN;
    }

}
