package io.avreen.common.util;

/**
 * The class Message diagnostic key.
 */
public class MessageDiagnosticKey {

    private static IMessageDiagnosticKeyProvider provider;


    /**
     * Put.
     *
     * @param key   the key
     * @param value the value
     */
    public static void put(String key, String value) {
        if (provider != null)
            provider.put(key, value);
    }

    /**
     * Get string.
     *
     * @param key the key
     * @return the string
     */
    public static String get(String key) {
        if (provider != null)
            return provider.get(key);
        return null;
    }

    /**
     * Remove.
     *
     * @param key the key
     */
    public static void remove(String key) {
        if (provider != null)
            provider.remove(key);
    }

    public static IMessageDiagnosticKeyProvider getProvider() {
        return provider;
    }

    public static void setProvider(IMessageDiagnosticKeyProvider provider) {
        MessageDiagnosticKey.provider = provider;
    }
}
