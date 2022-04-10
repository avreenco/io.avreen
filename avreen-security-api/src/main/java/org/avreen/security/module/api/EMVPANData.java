package org.avreen.security.module.api;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Emvpan data.
 */
public class EMVPANData implements IByteArrayConverter {
    private String PAN;
    private String PAN_SEQUENCE;

    /**
     * Instantiates a new Emvpan data.
     *
     * @param PAN          the pan
     * @param PAN_SEQUENCE the pan sequence
     */
    public EMVPANData(String PAN, String PAN_SEQUENCE) {
        this.PAN = PAN;
        this.PAN_SEQUENCE = PAN_SEQUENCE;
    }

    public EMVPANData(String PAN, int PAN_SEQUENCE) {
        this.PAN = PAN;
        byte[] bs = new byte[1];
        bs[0] = (byte) PAN_SEQUENCE;
        this.PAN_SEQUENCE = SecurityUtil.hexString(bs);
    }

    /**
     * Format pan info byte [ ].
     *
     * @param PAN    the pan
     * @param PANSQN the pansqn
     * @return the byte [ ]
     */
    public static byte[] formatPANInfo(String PAN, String PANSQN) {
        return formatPANInfo(SecurityUtil.hex2byte(PAN), Byte.parseByte(PANSQN, 10));

    }

    private static byte[] formatPANInfo(byte[] i_byaPAN, byte i_byPANSQN) {
        byte byaPANBlock[] = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        byte byaPANB[] = new byte[8];
        System.arraycopy(i_byaPAN, 0, byaPANBlock, 0, i_byaPAN.length);
        while ((byaPANBlock[9] & 0x0F) == 0x0F) {
            nibbleShift2Rigth(byaPANBlock, 10);
        }
        System.arraycopy(byaPANBlock, 3, byaPANB, 0, 7);
        byaPANB[7] = i_byPANSQN;
        return byaPANB;
    }

    private static void nibbleShift2Rigth(byte[] i_pbyString, int i_byStringLength) {
        int i;
        for (i = i_byStringLength - 1; i > 0; i--) {
            i_pbyString[i] = (byte) ((byte) ((i_pbyString[i] >> 4) & 0x0F) | (byte) ((i_pbyString[i - 1] << 4) & 0xF0));
        }
        i_pbyString[0] = (byte) ((i_pbyString[0] >> 4) & 0x0F);
    }

    /**
     * Gets pan.
     *
     * @return the pan
     */
    public String getPAN() {
        return PAN;
    }

    /**
     * Sets pan.
     *
     * @param PAN the pan
     */
    public void setPAN(String PAN) {
        this.PAN = PAN;
    }

    /**
     * Gets pan sequence.
     *
     * @return the pan sequence
     */
    public String getPAN_SEQUENCE() {
        return PAN_SEQUENCE;
    }

    /**
     * Sets pan sequence.
     *
     * @param PAN_SEQUENCE the pan sequence
     */
    public void setPAN_SEQUENCE(String PAN_SEQUENCE) {
        this.PAN_SEQUENCE = PAN_SEQUENCE;
    }

    @Override
    public byte[] encode() {
        return formatPANInfo(PAN, PAN_SEQUENCE);
    }

    @Override
    public void decode(byte[] encoded) {
        throw new RuntimeException("not support EMVPAN Data decode");
    }

}
