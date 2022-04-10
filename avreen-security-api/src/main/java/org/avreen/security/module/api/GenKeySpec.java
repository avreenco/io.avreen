package org.avreen.security.module.api;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Gen key spec.
 */
public class GenKeySpec {
    private String keySpec;
    private byte[] KVC;

    /**
     * Gets key spec.
     *
     * @return the key spec
     */
    public String getKeySpec() {
        return keySpec;
    }

    /**
     * Sets key spec.
     *
     * @param keySpec the key spec
     */
    public void setKeySpec(String keySpec) {
        this.keySpec = keySpec;
    }

    /**
     * Get kvc byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getKVC() {
        return KVC;
    }

    /**
     * Sets kvc.
     *
     * @param kVC the k vc
     */
    public void setKVC(byte[] kVC) {
        KVC = kVC;
    }

}
