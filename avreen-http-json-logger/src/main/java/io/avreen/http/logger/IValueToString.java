package io.avreen.http.logger;

/**
 * The interface Value to string.
 */
public interface IValueToString {
    /**
     * Value of string.
     *
     * @param fieldName the field name
     * @param obj       the obj
     * @return the string
     */
    String valueOf(String fieldName, Object obj);
}
