package org.avreen.security.module.api.response;

import org.avreen.security.module.api.ResponseBase;
import org.avreen.security.module.api.SafenetFieldType;
import org.avreen.security.module.api.SafenetItemAnnotaion;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Response ii key gen.
 */
public class Response_DERIVE_ICC_MASTER_KEY extends ResponseBase {
    @SafenetItemAnnotaion(order = 1, length = 16, type = SafenetFieldType.h)
    private byte[] eKTK_MK;
    @SafenetItemAnnotaion(order = 2, length = -1, type = SafenetFieldType.h)
    private byte[] KVC;

    public byte[] geteKTK_MK() {
        return eKTK_MK;
    }

    public void seteKTK_MK(byte[] eKTK_MK) {
        this.eKTK_MK = eKTK_MK;
    }

    public byte[] getKVC() {
        return KVC;
    }

    public void setKVC(byte[] KVC) {
        this.KVC = KVC;
    }
}
