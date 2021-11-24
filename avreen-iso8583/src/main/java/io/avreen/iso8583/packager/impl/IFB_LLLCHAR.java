package io.avreen.iso8583.packager.impl;

/**
 * The class Ifb lllchar.
 */
public class IFB_LLLCHAR extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifb lllchar.
     *
     * @param len         the len
     * @param description the description
     */
    public IFB_LLLCHAR(int len, String description) {
        super(len, description, null, AsciiInterpreter.INSTANCE, BCDPrefixer.LLL);
        checkLength(len, 999);
    }

    public void setLength(int len) {
        checkLength(len, 999);
        super.setLength(len);
    }
}
