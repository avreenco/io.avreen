package io.avreen.http.channel;

import io.avreen.common.netty.client.NettyClientBase;
import io.avreen.http.common.HttpMsg;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * The type Http netty client.
 */
public class HttpNettyClient extends NettyClientBase<HttpMsg> {

    private ClientHttpMessageCodec clientHttpMessageCodec;
    /**
     * The constant ActorType.
     */
    public static final String   ActorType="http-client";

    private HttpClientCodecSetting httpCodecSetting;


    /**
     * Instantiates a new Http netty client.
     */
    public HttpNettyClient() {

    }

    /**
     * Builder http client builder.
     *
     * @return the http client builder
     */
    public static HttpNettyClientBuilder Builder() {
        return new HttpNettyClientBuilder();
    }

    public HttpClientCodecSetting getHttpCodecSetting() {
        if(httpCodecSetting==null)
            httpCodecSetting = HttpClientCodecSetting.createDefault();
        return httpCodecSetting;
    }

    public HttpNettyClient setHttpCodecSetting(HttpClientCodecSetting httpCodecSetting) {
        this.httpCodecSetting = httpCodecSetting;
        return this;
    }

    private HttpMessageCodec getCodec() {
        if (clientHttpMessageCodec == null) {
            clientHttpMessageCodec = new ClientHttpMessageCodec(getChannelId(), this);
        }
        return clientHttpMessageCodec;
    }


    @Override
    protected void initPipeline(ChannelPipeline p) {
        HttpClientCodecSetting httpCodecSetting = getHttpCodecSetting();

        p.addLast(new HttpClientCodec(httpCodecSetting.getMaxInitialLineLength(),httpCodecSetting.getMaxHeaderSize() ,httpCodecSetting.getMaxChunkSize(),httpCodecSetting.isFailOnMissingResponse(),httpCodecSetting.isValidateHeaders(),httpCodecSetting.isParseHttpAfterConnectRequest()));
        // add gizp compressor for http response content
        p.addLast(new HttpContentDecompressor(httpCodecSetting.isDecompressorStrict()));

        p.addLast(new HttpObjectAggregator(httpCodecSetting.getMaxContentLength()));

        p.addLast(new ChunkedWriteHandler());

        p.addLast(getCodec());


    }



    @Override
    public String getType() {
        return ActorType;
    }

}
