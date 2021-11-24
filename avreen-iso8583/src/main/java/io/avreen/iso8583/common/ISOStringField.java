package io.avreen.iso8583.common;

/**
 * The class Iso string field.
 */
public class ISOStringField
        extends ISOField<String> {

    /**
     * The Value.
     */
    protected String value;

    /**
     * Instantiates a new Iso string field.
     */
    public ISOStringField() {
    }

    /**
     * Instantiates a new Iso string field.
     *
     * @param v the v
     */
    public ISOStringField(String v) {
        value = v;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String obj) {
        value = obj;
    }

    @Override
    public String getType() {
        return "string";
    }

    @Override
    public String toString() {
        return value;
    }
}
