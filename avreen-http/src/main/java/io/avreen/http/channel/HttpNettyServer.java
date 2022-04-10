
package io.avreen.http.channel;

import io.avreen.common.netty.server.NettyServerBase;
import io.avreen.http.common.HttpMsg;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.Set;


/**
 * The type Http netty server.
 */
public class HttpNettyServer extends NettyServerBase<HttpMsg> {

    private ServerHttpMessageCodec serverHttpMessageCodec;

    private IHttpRoutResolver routResolver;


    private HttpServerCodecSetting httpCodecSetting;

    private Set<String> traceAbleHeaderNames;

    /**
     * Instantiates a new Http netty server.
     */
    public HttpNettyServer() {

    }

    /**
     * Builder http netty server builder.
     *
     * @return the http netty server builder
     */
    public static HttpNettyServerBuilder Builder() {
        return new HttpNettyServerBuilder();
    }

    private HttpMessageCodec getCodec() {
        if (serverHttpMessageCodec == null) {
            serverHttpMessageCodec = new ServerHttpMessageCodec(getChannelId(), this, routResolver, traceAbleHeaderNames);
        }
        return serverHttpMessageCodec;
    }

    public IHttpRoutResolver getRoutResolver() {
        return routResolver;
    }

    public HttpNettyServer setRoutResolver(IHttpRoutResolver routResolver) {
        this.routResolver = routResolver;
        return this;
    }

    public HttpServerCodecSetting getHttpCodecSetting() {
        if (httpCodecSetting == null)
            httpCodecSetting = HttpServerCodecSetting.createDefault();
        return httpCodecSetting;
    }

    public HttpNettyServer setHttpCodecSetting(HttpServerCodecSetting httpCodecSetting) {
        this.httpCodecSetting = httpCodecSetting;
        return this;
    }

    public HttpNettyServer setTraceAbleHeaderNames(Set<String> traceAbleHeaderNames) {
        this.traceAbleHeaderNames = traceAbleHeaderNames;
        return this;
    }

    @Override
    protected void initPipeline(ChannelPipeline p) {
        HttpServerCodecSetting httpCodecSetting = getHttpCodecSetting();
        p.addLast(new HttpServerCodec(httpCodecSetting.getMaxInitialLineLength(), httpCodecSetting.getMaxHeaderSize(), httpCodecSetting.getMaxChunkSize()));
        // add gizp compressor for http response content
        p.addLast(new HttpContentCompressor(httpCodecSetting.getCompressorLevel(), httpCodecSetting.getCompressorWindowBits(), httpCodecSetting.getCompressorMemLevel(), httpCodecSetting.getCompressorContentSizeThreshold()));

        p.addLast(new HttpObjectAggregator(httpCodecSetting.getMaxContentLength()));

        p.addLast(new ChunkedWriteHandler());
        p.addLast(getCodec());
    }


    @Override
    public String getType() {
        return "http-server";
    }
}

