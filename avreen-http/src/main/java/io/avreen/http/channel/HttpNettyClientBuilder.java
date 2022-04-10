package io.avreen.http.channel;


import io.avreen.common.netty.client.NettyClientBaseBuilder;
import io.avreen.http.common.HttpMsg;


/**
 * The type Http client builder.
 */
public class HttpNettyClientBuilder extends NettyClientBaseBuilder<HttpNettyClient, HttpMsg, HttpNettyClientBuilder> {
    /**
     * Instantiates a new Http client builder.
     */
    protected HttpNettyClientBuilder() {
        super(new HttpNettyClient());
    }

    /**
     * Builder http client builder.
     *
     * @return the http client builder
     */
    public static HttpNettyClientBuilder Builder() {
        return new HttpNettyClientBuilder();
    }

    public HttpNettyClientBuilder setHttpCodecSetting(HttpClientCodecSetting httpCodecSetting) {
        delegate.setHttpCodecSetting(httpCodecSetting);
        return this;
    }



    public HttpNettyClient build() {
        super.build();
        delegate.setCloseWhenReadComplete(true);
        return delegate;
    }
}

