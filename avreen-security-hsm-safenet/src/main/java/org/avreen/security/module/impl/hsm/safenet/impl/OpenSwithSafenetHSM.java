package org.avreen.security.module.impl.hsm.safenet.impl;

import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.ISecurityModule;
import org.avreen.security.module.api.SecurityFunctionMessage;
import org.avreen.security.module.api.request.*;
import org.avreen.security.module.api.response.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Open swith safenet hsm.
 */
//@Component("security.module.safenet")
//@ManagedResource(objectName = "org.avreen:type=security,name=safenet")
public class OpenSwithSafenetHSM implements ISecurityModule {
    private int timeout = 10000;
    private IMultiplexer<SecurityFunctionMessage> multiplexer;

    /**
     * Instantiates a new Open swith safenet hsm.
     *
     * @param multiplexer the multiplexer
     */
    public OpenSwithSafenetHSM(IMultiplexer<SecurityFunctionMessage> multiplexer) {
        this.multiplexer = multiplexer;
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public Response_ADVANCE_RANDOM_KEY_GEN ADVANCE_RANDOM_KEY_GEN(Request_ADVANCE_RANDOM_KEY_GEN request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_PIN_TRAN_2 PIN_TRAN_2(Request_PIN_TRAN_2 request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);

    }

    @Override
    public Response_ENCIPHER_2 ENCIPHER_2(Request_ENCIPHER_2 request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_DECIPHER_2 DECIPHER_2(Request_DECIPHER_2 request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_DECIPHER_4 DECIPHER_4(Request_DECIPHER_4 request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_MAC_GEN_FINAL MAC_GEN_FINAL(Request_MAC_GEN_FINAL request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_MAC_VER_FINAL MAC_VER_FINAL(Request_MAC_VER_FINAL request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_PIN_VER_IBM_MULTI PIN_VER_IBM_MULTI(Request_PIN_VER_IBM_MULTI request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_II_KEY_GEN II_KEY_GEN(Request_II_KEY_GEN request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_II_KEY_RCV II_KEY_RCV(Request_II_KEY_RCV request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_IT_KEY_GEN IT_KEY_GEN(Request_IT_KEY_GEN request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_DUKPT_IK_Derive_2 DUKPT_IK_Derive_2(Request_DUKPT_IK_Derive_2 request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_PIN_OFF PIN_OFF(Request_PIN_OFF request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_EMV_SCRIPT_CRYPTO_EMV2000 EMV_SCRIPT_CRYPTO_EMV2000(Request_EMV_SCRIPT_CRYPTO_EMV2000 request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_EMV_VERIFY_AC_EMV2000 EMV_VERIFY_AC_EMV2000(Request_EMV_VERIFY_AC_EMV2000 request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_EMV_VERIFY_AC_VISA EMV_VERIFY_AC_VISA(Request_EMV_VERIFY_AC_VISA request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_EMV_GENERATE_ARPC EMV_GENERATE_ARPC(Request_EMV_GENERATE_ARPC request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_CVV_GENERATE CVV_GENERATE(Request_CVV_GENERATE request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_CVV_VERIFY CVV_VERIFY(Request_CVV_VERIFY request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_GENERATE_HASH GENERATE_HASH(Request_GENERATE_HASH request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }

    @Override
    public Response_DERIVE_ICC_MASTER_KEY DERIVE_ICC_MASTER_KEY(Request_DERIVE_ICC_MASTER_KEY request) {
        return SafeNetMessageSender.send(multiplexer, request, timeout);
    }
}
