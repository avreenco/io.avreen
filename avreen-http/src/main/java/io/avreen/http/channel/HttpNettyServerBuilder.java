package io.avreen.http.channel;

import io.avreen.common.netty.server.NettyServerBaseBuilder;
import io.avreen.http.common.HttpMsg;

import java.util.Set;


/**
 * The type Http netty server builder.
 */
public class HttpNettyServerBuilder extends NettyServerBaseBuilder<HttpNettyServer, HttpMsg, HttpNettyServerBuilder> {
    /**
     * Instantiates a new Http netty server builder.
     */


    protected HttpNettyServerBuilder() {
        super(new HttpNettyServer());

    }

    public HttpNettyServerBuilder setRoutResolver(IHttpRoutResolver routResolver) {
        nettyServer.setRoutResolver(routResolver);
        return this;
    }

    public HttpNettyServerBuilder setHttpCodecSetting(HttpServerCodecSetting httpCodecSetting) {
        nettyServer.setHttpCodecSetting(httpCodecSetting);
        return this;
    }

    public HttpNettyServerBuilder setTraceAbleHeaderNames(Set<String> traceAbleHeaderNames) {
        nettyServer.setTraceAbleHeaderNames(traceAbleHeaderNames);
        return this;
    }


    /**
     * Builder http netty server builder.
     *
     * @return the http netty server builder
     */
    public static HttpNettyServerBuilder Builder() {
        return new HttpNettyServerBuilder();
    }


    public HttpNettyServer build() {
        super.build();
        return this.nettyServer;
    }

}
