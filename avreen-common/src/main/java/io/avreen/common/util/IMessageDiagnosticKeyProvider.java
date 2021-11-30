package io.avreen.common.util;

/**
 * The class Message diagnostic key.
 */
public interface IMessageDiagnosticKeyProvider {

    void put(String key, String value);

    String get(String key);

    void remove(String key);
}
