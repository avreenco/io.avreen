package io.avreen.iso8583.common;

import java.io.PrintStream;
import java.io.Serializable;

/**
 * The interface Iso component dumper.
 */
public interface ISOComponentDumper extends Serializable {
    /**
     * The constant ID_ATTR.
     */
    String ID_ATTR = "id";
    /**
     * The constant VALUE_ATTR.
     */
    String VALUE_ATTR = "value";
    /**
     * The constant TYPE_ATTR.
     */
    String TYPE_ATTR = "type";

    /**
     * Dump.
     *
     * @param fieldNumber  the field number
     * @param isoComponent the iso component
     * @param p            the p
     * @param indent       the indent
     */
    void dump(int fieldNumber, ISOComponent isoComponent, PrintStream p, String indent);

    /**
     * Convert to string string.
     *
     * @param isoComponent the iso component
     * @return the string
     */
    String convertToString(ISOComponent isoComponent);
}
