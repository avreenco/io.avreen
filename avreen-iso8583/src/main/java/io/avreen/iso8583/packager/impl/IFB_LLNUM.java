package io.avreen.iso8583.packager.impl;

/**
 * The class Ifb llnum.
 */
public class IFB_LLNUM extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifb llnum.
     *
     * @param len          the len
     * @param description  the description
     * @param isLeftPadded the is left padded
     */
    public IFB_LLNUM(int len, String description, boolean isLeftPadded) {
        super(len, description, null,
                isLeftPadded ? BCDInterpreter.LEFT_PADDED : BCDInterpreter.RIGHT_PADDED,
                BCDPrefixer.LL);
        checkLength(len, 99);
    }

    /**
     * Instantiates a new Ifb llnum.
     *
     * @param len          the len
     * @param description  the description
     * @param isLeftPadded the is left padded
     * @param fPadded      the f padded
     */
    public IFB_LLNUM(int len, String description, boolean isLeftPadded, boolean fPadded) {
        super(len, description, null,
                isLeftPadded ? BCDInterpreter.LEFT_PADDED :
                        fPadded ? BCDInterpreter.RIGHT_PADDED_F : BCDInterpreter.RIGHT_PADDED,
                BCDPrefixer.LL);
        checkLength(len, 99);
    }

    public void setLength(int len) {
        checkLength(len, 99);
        super.setLength(len);
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

