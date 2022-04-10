package org.avreen.security.module.api.response;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response pin off.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.PIN_OFF)
public class Response_PIN_OFF extends ResponseBase {
    @SafenetItemAnnotaion(order = 1, type = SafenetFieldType.h)
    private byte[] Offset;
    @SafenetItemAnnotaion(order = 1, type = SafenetFieldType.h)
    private short PINLEN;

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
     * Gets pinlen.
     *
     * @return the pinlen
     */
    public short getPINLEN() {
        return PINLEN;
    }

    /**
     * Sets pinlen.
     *
     * @param pINLEN the p inlen
     */
    public void setPINLEN(short pINLEN) {
        PINLEN = pINLEN;
    }

}
