package org.avreen.security.module.impl.hsm.safenet.channel;


import io.avreen.common.IMessageKeyProvider;
import org.avreen.security.module.api.SecurityFunctionMessage;
import org.avreen.security.module.api.SecurityUtil;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Safe net mux key provider.
 */
public class SafeNetMUXKeyProvider implements IMessageKeyProvider<SecurityFunctionMessage> {

    /**
     * The constant INSTANCE.
     */
    public static SafeNetMUXKeyProvider INSTANCE = new SafeNetMUXKeyProvider();


    private SafeNetMUXKeyProvider() {

    }

    public String getKey(SecurityFunctionMessage m, String out, boolean outgoing) {
        StringBuilder sb = new StringBuilder(out);
        sb.append(SecurityUtil.hexString(m.getHeader()));
        return sb.toString();
    }


}
