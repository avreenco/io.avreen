package org.avreen.security.module.impl.hsm.safenet.impl;

import io.avreen.common.context.MsgContext;
import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.RequestBase;
import org.avreen.security.module.api.ResponseBase;
import org.avreen.security.module.api.SecurityFunctionMessage;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Safe net message sender.
 */
public class SafeNetMessageSender {
    /**
     * Send t.
     *
     * @param <T>         the type parameter
     * @param multiplexer the multiplexer
     * @param req         the req
     * @param timeout     the timeout
     * @return the t
     */
    public static <T extends ResponseBase> T send(IMultiplexer<SecurityFunctionMessage> multiplexer, RequestBase req, int timeout) {
        if (req.getHeader() == null)
            req.buildHeader();
        SecurityFunctionMessage resp = multiplexer.request(new MsgContext<>("safenet",req) , timeout);
        if (resp == null)
            throw new RuntimeException("timeout from hsm");
        return (T) resp;

    }
}
