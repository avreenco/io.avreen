package io.avreen.http.common;

public interface IUriMatcher {
    boolean match(String pattern, String uri);
}
