package io.avreen.iso8583.packager.impl;

/**
 * The class Ifb numeric.
 */
public class IFB_NUMERIC extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifb numeric.
     *
     * @param len          the len
     * @param description  the description
     * @param isLeftPadded the is left padded
     */
    public IFB_NUMERIC(int len, String description, boolean isLeftPadded) {
        super(len, description, LeftPadder.ZERO_PADDER,
                isLeftPadded ? BCDInterpreter.LEFT_PADDED : BCDInterpreter.RIGHT_PADDED,
                null);
    }

    /**
     * Sets pad.
     *
     * @param pad the pad
     */
    public void setPad(boolean pad) {
        setInterpreter(pad ? BCDInterpreter.LEFT_PADDED : BCDInterpreter.RIGHT_PADDED);
    }
}
