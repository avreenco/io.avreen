package io.avreen.iso8583.packager.impl;

/**
 * The class Ifb llchar.
 */
public class IFB_LLCHAR extends ISOStringFieldPackager {
    /**
     * Instantiates a new Ifb llchar.
     *
     * @param len         the len
     * @param description the description
     */
    public IFB_LLCHAR(int len, String description) {
        super(len, description, null, AsciiInterpreter.INSTANCE, BCDPrefixer.LL);
        checkLength(len, 99);
    }

    public void setLength(int len) {
        checkLength(len, 99);
        super.setLength(len);
    }
}
