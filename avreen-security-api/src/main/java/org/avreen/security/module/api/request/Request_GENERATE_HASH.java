package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request generate hash.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.GEN_HASH)
public class Request_GENERATE_HASH extends RequestBase {
    @SafenetItemAnnotaion(length = 1, order = 1, type = SafenetFieldType.h)
    private byte FM = 0;
    @SafenetItemAnnotaion(length = 1, order = 2, type = SafenetFieldType.h)
    private HashAlgorithms Algorithm;
    @SafenetItemAnnotaion(length = 1, order = 3, type = SafenetFieldType.h)
    private byte Mode;
    @SafenetItemAnnotaion(length = 8, order = 4, type = SafenetFieldType.h)
    private byte[] BitCount;
    @SafenetItemAnnotaion(order = 5, type = SafenetFieldType.h)
    private byte[] HashValue;
    @SafenetItemAnnotaion(order = 6, type = SafenetFieldType.h)
    private byte[] Data;

    /**
     * Instantiates a new Request generate hash.
     */
    public Request_GENERATE_HASH() {
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
     * @param FM the fm
     */
    public void setFM(byte FM) {
        this.FM = FM;
    }

    /**
     * Gets algorithm.
     *
     * @return the algorithm
     */
    public HashAlgorithms getAlgorithm() {
        return Algorithm;
    }

    /**
     * Sets algorithm.
     *
     * @param algorithm the algorithm
     */
    public void setAlgorithm(HashAlgorithms algorithm) {
        Algorithm = algorithm;
    }

    /**
     * Gets mode.
     *
     * @return the mode
     */
    public byte getMode() {
        return Mode;
    }

    /**
     * Sets mode.
     *
     * @param mode the mode
     */
    public void setMode(byte mode) {
        Mode = mode;
    }

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
     * Get hash value byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getHashValue() {
        return HashValue;
    }

    /**
     * Sets hash value.
     *
     * @param hashValue the hash value
     */
    public void setHashValue(byte[] hashValue) {
        HashValue = hashValue;
    }

    /**
     * Get data byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getData() {
        return Data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(byte[] data) {
        Data = data;
    }
}
