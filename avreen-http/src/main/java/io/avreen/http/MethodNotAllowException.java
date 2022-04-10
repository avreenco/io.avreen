package io.avreen.http;

public class MethodNotAllowException extends Exception {

    public MethodNotAllowException(String uri) {
        super("uri not allow. uri=" + uri);
        this.uri = uri;
    }

    private String uri;

    public String getUri() {
        return uri;
    }
}
