package org.avreen.security.module.api;


import java.util.concurrent.atomic.AtomicLong;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request base.
 */
public class RequestBase extends SecurityFunctionMessage
{

    private static AtomicLong atomicLong = new AtomicLong(0x0000);

    private synchronized static int getTxnSeq() {
        long seq = atomicLong.incrementAndGet();
        long max = 0xFFFF;
        return (int) (seq % max) + 1;
    }

    /**
     * Instantiates a new Request base.
     */
    public RequestBase() {

    }

    /**
     * Build header.
     */
    public void buildHeader() {
        byte[] b = new byte[4];
        b[0] = 0x01;
        b[1] = 0x01;
        byte[] hex = SecurityUtil.hex2byte(SecurityUtil.padleft(Integer.toHexString(getTxnSeq()), 4, '0'));
        System.arraycopy(hex, 0, b, 2, hex.length);
        setHeader(b);
    }

}
