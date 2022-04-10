package io.avreen.http.channel;


import io.avreen.common.IMessageKeyProvider;
import io.avreen.http.common.HttpMsg;

/**
 * The type Http message key provider.
 */
public class HttpMessageKeyProvider implements IMessageKeyProvider<HttpMsg> {


    /**
     * Instantiates a new Http message key provider.
     */
    public HttpMessageKeyProvider() {
    }

    public String getKey(HttpMsg m, String out, boolean outgoing) {
        if (m instanceof IMessageKeyProvider)
            return ((IMessageKeyProvider) m).getKey(m, out, outgoing);
        throw new RuntimeException("message not support message key provider for mux");
    }

}
