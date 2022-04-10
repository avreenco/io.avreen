package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request emv generate arpc.
 */
//Page 268
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.DERIVE_ICC_MASTER_KEY)
public class Request_DERIVE_ICC_MASTER_KEY extends RequestBase {
    @SafenetItemAnnotaion(order = 1, length = 1, type = SafenetFieldType.h)
    private byte FM = 0;
    @SafenetItemAnnotaion(order = 2, length = -1, type = SafenetFieldType.K_Spec)
    private String IMKAC_Spec;
    @SafenetItemAnnotaion(order = 3, length = 1, type = SafenetFieldType.h)
    private IMKTypes IMK_Types;
    @SafenetItemAnnotaion(order = 4, length = 8, type = SafenetFieldType.h)
    private EMVPANData PAN_Data;
    @SafenetItemAnnotaion(order = 5, length = -1, type = SafenetFieldType.K_Spec)
    private String KTK_Spec;
    @SafenetItemAnnotaion(order = 6, length = 1, type = SafenetFieldType.h)
    private EncryptionStandard Encryption_Method;
    @SafenetItemAnnotaion(order = 7, length = 1, type = SafenetFieldType.h)
    private KVCMethod KVC_Method;
    /**
     * Instantiates a new Request emv generate arpc.
     */
    public Request_DERIVE_ICC_MASTER_KEY() {
        super();
    }

    public byte getFM() {
        return FM;
    }

    public void setFM(byte FM) {
        this.FM = FM;
    }

    public String getIMKAC_Spec() {
        return IMKAC_Spec;
    }

    public void setIMKAC_Spec(String IMKAC_Spec) {
        this.IMKAC_Spec = IMKAC_Spec;
    }

    public IMKTypes getIMK_Types() {
        return IMK_Types;
    }

    public void setIMK_Types(IMKTypes IMK_Types) {
        this.IMK_Types = IMK_Types;
    }

    public EMVPANData getPAN_Data() {
        return PAN_Data;
    }

    public void setPAN_Data(EMVPANData PAN_Data) {
        this.PAN_Data = PAN_Data;
    }

    public String getKTK_Spec() {
        return KTK_Spec;
    }

    public void setKTK_Spec(String KTK_Spec) {
        this.KTK_Spec = KTK_Spec;
    }

    public EncryptionStandard getEncryption_Method() {
        return Encryption_Method;
    }

    public void setEncryption_Method(EncryptionStandard encryption_Method) {
        Encryption_Method = encryption_Method;
    }

    public KVCMethod getKVC_Method() {
        return KVC_Method;
    }

    public void setKVC_Method(KVCMethod KVC_Method) {
        this.KVC_Method = KVC_Method;
    }
}
