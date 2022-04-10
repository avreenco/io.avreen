package io.avreen.http.common;

public class EqualUriMatcher implements IUriMatcher {

    @Override
    public boolean match(String pattern, String uri) {
        return pattern.equalsIgnoreCase(uri);
    }
}
