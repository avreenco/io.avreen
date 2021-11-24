package io.avreen.iso8583.packager.impl;


/**
 * The class Ifb llbinary.
 */
public class IFB_LLBINARY extends ISOBinaryFieldPackager {
    /**
     * Instantiates a new Ifb llbinary.
     *
     * @param len         the len
     * @param description the description
     */
    public IFB_LLBINARY(int len, String description) {
        super(len, description, LiteralBinaryInterpreter.INSTANCE, BCDPrefixer.LL);
        checkLength(len, 99);
    }

    public void setLength(int len) {
        checkLength(len, 99);
        super.setLength(len);
    }
}
