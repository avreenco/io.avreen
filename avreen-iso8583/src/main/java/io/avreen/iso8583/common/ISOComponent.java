package io.avreen.iso8583.common;

import java.io.Serializable;

/**
 * The interface Iso component.
 *
 * @param <T> the type parameter
 */
public interface ISOComponent<T> extends Cloneable, Serializable {
    /**
     * Gets value.
     *
     * @return the value
     */
    T getValue();

    /**
     * Sets value.
     *
     * @param obj the obj
     */
    void setValue(T obj);

    /**
     * Gets type.
     *
     * @return the type
     */
    String getType();

    /**
     * Gets dumper.
     *
     * @return the dumper
     */
    ISOComponentDumper getDumper();


}
