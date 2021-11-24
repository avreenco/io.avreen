package io.avreen.iso8583.packager.impl;

/**
 * The class Ifb lllbinary.
 */
public class IFB_LLLBINARY extends ISOBinaryFieldPackager {
    /**
     * Instantiates a new Ifb lllbinary.
     *
     * @param len         the len
     * @param description the description
     */
    public IFB_LLLBINARY(int len, String description) {
        super(len, description, LiteralBinaryInterpreter.INSTANCE, BCDPrefixer.LLL);
        checkLength(len, 999);
    }

    public void setLength(int len) {
        checkLength(len, 999);
        super.setLength(len);
    }
}
