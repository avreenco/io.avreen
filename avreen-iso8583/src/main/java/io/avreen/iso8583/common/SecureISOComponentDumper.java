package io.avreen.iso8583.common;

import io.avreen.iso8583.util.DumpUtil;

import java.io.PrintStream;

/**
 * The class Secure iso component dumper.
 */
public class SecureISOComponentDumper implements ISOComponentDumper {
    private static SecureISOComponentDumper instance = new SecureISOComponentDumper();

    private String securityString = "[*SECURE*]";

    /**
     * Instantiates a new Secure iso component dumper.
     */
    protected SecureISOComponentDumper() {

    }

    /**
     * Instance secure iso component dumper.
     *
     * @return the secure iso component dumper
     */
    public static SecureISOComponentDumper INSTANCE() {
        return instance;
    }

    /**
     * Gets security string.
     *
     * @return the security string
     */
    public String getSecurityString() {
        return securityString;
    }

    /**
     * Sets security string.
     *
     * @param securityString the security string
     */
    public void setSecurityString(String securityString) {
        this.securityString = securityString;
    }

    @Override
    public void dump(int fieldNumber, ISOComponent isoComponent, PrintStream p, String indent) {
        if (isoComponent.getValue() == null)
            return;
        p.print(indent);
        p.print("{ ");
        p.print(DumpUtil.buildItem(ISOComponentDumper.ID_ATTR, fieldNumber));
        p.print(" , ");
        p.print(DumpUtil.buildItem(ISOComponentDumper.TYPE_ATTR, isoComponent.getType()));
        p.print(" , ");
        p.print(DumpUtil.buildItem(ISOComponentDumper.VALUE_ATTR, getSecurityString()));
        p.print("}");
    }

    @Override
    public String convertToString(ISOComponent isoComponent) {
        return getSecurityString();
    }
}
