package org.avreen.security.module.api.response;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response generate hash.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.GEN_HASH)
public class Response_GENERATE_HASH extends ResponseBase {
    @SafenetItemAnnotaion(order = 1, length = 8, type = SafenetFieldType.h)
    private byte[] BitCount;
    @SafenetItemAnnotaion(order = 2, type = SafenetFieldType.h)
    private byte[] HashResult;

    /**
     * Get bit count byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getBitCount() {
        return BitCount;
    }

    /**
     * Sets bit count.
     *
     * @param bitCount the bit count
     */
    public void setBitCount(byte[] bitCount) {
        BitCount = bitCount;
    }

    /**
     * Get hash result byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getHashResult() {
        return HashResult;
    }

    /**
     * Sets hash result.
     *
     * @param hashResult the hash result
     */
    public void setHashResult(byte[] hashResult) {
        HashResult = hashResult;
    }
}
