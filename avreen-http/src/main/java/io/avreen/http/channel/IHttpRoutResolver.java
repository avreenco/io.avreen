package io.avreen.http.channel;

import io.avreen.http.MethodNotAllowException;

public interface IHttpRoutResolver
{
    RoutResolverResult    resolve(String  uri ) throws MethodNotAllowException;
}
