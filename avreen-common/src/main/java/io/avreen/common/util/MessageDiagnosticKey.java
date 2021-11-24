package io.avreen.common.util;

import org.slf4j.MDC;

/**
 * The class Message diagnostic key.
 */
public class MessageDiagnosticKey {


    /**
     * Put.
     *
     * @param key   the key
     * @param value the value
     */
    public static void put(String key, String value) {
        MDC.put(key, value);
    }

    /**
     * Get string.
     *
     * @param key the key
     * @return the string
     */
    public static String get(String key) {
        return MDC.get(key);
    }

    /**
     * Remove.
     *
     * @param key the key
     */
    public static void remove(String key) {
        MDC.remove(key);
    }
}
