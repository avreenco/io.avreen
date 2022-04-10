package org.avreen.security.module.impl.hsm.safenet.channel;

import org.avreen.security.module.api.SecurityFunctionMessage;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test scan.
 */
public class testScan {
    /**
     * The entry point of application.
     *
     * @param a the input arguments
     */
    public static void main(String[] a) {
        SecurityFunctionMessage securityFunctionMessage = SecurityFunctionMessageRepository.buildRequestMessage("EE0702");
        securityFunctionMessage = SecurityFunctionMessageRepository.buildRequestMessage("EE0702");
        System.out.println(securityFunctionMessage.getClass().getSimpleName());
    }
}
