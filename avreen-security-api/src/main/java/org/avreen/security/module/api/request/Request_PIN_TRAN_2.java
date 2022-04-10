package org.avreen.security.module.api.request;

import org.avreen.security.module.api.*;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Request pin tran 2.
 */
@SafenetFunctionCodeAnnotaion(code = FunctionCodes.PIN_TRAN_2)
public class Request_PIN_TRAN_2 extends RequestBase {
    @SafenetItemAnnotaion(order = 1, length = 1, type = SafenetFieldType.h)
    private byte FM;
    @SafenetItemAnnotaion(order = 2, length = 8, type = SafenetFieldType.h)
    private byte[] ePPKi_PIN;
    @SafenetItemAnnotaion(order = 3, type = SafenetFieldType.K_Spec)
    private String PPKi_Spec;
    @SafenetItemAnnotaion(order = 4, length = 1, type = SafenetFieldType.h)
    private PinFormat PFi;
    @SafenetItemAnnotaion(order = 5, length = 6, type = SafenetFieldType.h)
    private byte[] ANB;
    @SafenetItemAnnotaion(order = 6, length = 1, type = SafenetFieldType.h)
    private PinFormat PFo;
    @SafenetItemAnnotaion(order = 7, type = SafenetFieldType.K_Spec)
    private String PPKo_Spec;
    private EncryptionStandard PiEncryptionStandard = EncryptionStandard.DES;
    private EncryptionStandard PoEncryptionStandard = EncryptionStandard.DES;

    /**
     * Gets pi encryption standard.
     *
     * @return the pi encryption standard
     */
    public EncryptionStandard getPiEncryptionStandard() {
        return PiEncryptionStandard;
    }

    /**
     * Sets pi encryption standard.
     *
     * @param piEncryptionStandard the pi encryption standard
     */
    public void setPiEncryptionStandard(EncryptionStandard piEncryptionStandard) {
        PiEncryptionStandard = piEncryptionStandard;
    }

    /**
     * Gets po encryption standard.
     *
     * @return the po encryption standard
     */
    public EncryptionStandard getPoEncryptionStandard() {
        return PoEncryptionStandard;
    }

    /**
     * Sets po encryption standard.
     *
     * @param poEncryptionStandard the po encryption standard
     */
    public void setPoEncryptionStandard(EncryptionStandard poEncryptionStandard) {
        PoEncryptionStandard = poEncryptionStandard;
    }

    /**
     * Instantiates a new Request pin tran 2.
     */
    public Request_PIN_TRAN_2() {
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
     * @param fM the f m
     */
    public void setFM(byte fM) {
        FM = fM;
    }

    /**
     * Gete pp ki pin byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getePPKi_PIN() {
        return ePPKi_PIN;
    }

    /**
     * Sets pp ki pin.
     *
     * @param ePPKi_PIN the e pp ki pin
     */
    public void setePPKi_PIN(byte[] ePPKi_PIN) {
        this.ePPKi_PIN = ePPKi_PIN;
    }

    /**
     * Gets pp ki spec.
     *
     * @return the pp ki spec
     */
    public String getPPKi_Spec() {
        return PPKi_Spec;
    }

    /**
     * Sets pp ki spec.
     *
     * @param pPKi_Spec the p p ki spec
     */
    public void setPPKi_Spec(String pPKi_Spec) {
        PPKi_Spec = pPKi_Spec;
    }

    /**
     * Sets pp ki spec.
     *
     * @param BDK_Spec the bdk spec
     * @param ksn      the ksn
     */
    public void setPPKi_Spec(String BDK_Spec, String ksn) {
        PPKi_Spec = buildFormat20KeySpec(BDK_Spec, ksn);
    }

    private String buildFormat20KeySpec(String bdkKeySpec, String ksn) {
/*		StringBuffer buffer = new StringBuffer();
        buffer.append("20");
		buffer.append(ByteUtil.padleft(new Integer(bdkKeySpec.length()/2).toString(),2,'0'));
		buffer.append(bdkKeySpec);
		buffer.append(ksn);
		buffer.append("02");
		return buffer.toString();*/
        return "20";
    }

    /**
     * Gets p fi.
     *
     * @return the p fi
     */
    public PinFormat getPFi() {
        return PFi;
    }

    /**
     * Sets p fi.
     *
     * @param pFi the p fi
     */
    public void setPFi(PinFormat pFi) {
        PFi = pFi;
    }

    /**
     * Get anb byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getANB() {
        return ANB;
    }

    /**
     * Sets anb.
     *
     * @param aNB the a nb
     */
    public void setANB(byte[] aNB) {
        ANB = aNB;
    }

    /**
     * Gets p fo.
     *
     * @return the p fo
     */
    public PinFormat getPFo() {
        return PFo;
    }

    /**
     * Sets p fo.
     *
     * @param pFo the p fo
     */
    public void setPFo(PinFormat pFo) {
        PFo = pFo;
    }

    /**
     * Gets pp ko spec.
     *
     * @return the pp ko spec
     */
    public String getPPKo_Spec() {
        return PPKo_Spec;
    }

    /**
     * Sets pp ko spec.
     *
     * @param pPKo_Spec the p p ko spec
     */
    public void setPPKo_Spec(String pPKo_Spec) {
        PPKo_Spec = pPKo_Spec;
    }

}
