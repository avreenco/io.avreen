package io.avreen.http.channel;


import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectDecoder;

/**
 * The class Http server properties.
 */
public class HttpClientCodecSetting {

    private int maxInitialLineLength = HttpObjectDecoder.DEFAULT_MAX_INITIAL_LINE_LENGTH;
    private int maxHeaderSize = HttpObjectDecoder.DEFAULT_MAX_HEADER_SIZE;
    private int maxChunkSize = HttpObjectDecoder.DEFAULT_MAX_CHUNK_SIZE;
    private int maxContentLength = Integer.MAX_VALUE;
    private boolean failOnMissingResponse = HttpClientCodec.DEFAULT_FAIL_ON_MISSING_RESPONSE;
    private boolean decompressorStrict = false;
    private boolean validateHeaders = HttpObjectDecoder.DEFAULT_VALIDATE_HEADERS;
    private boolean parseHttpAfterConnectRequest = HttpClientCodec.DEFAULT_PARSE_HTTP_AFTER_CONNECT_REQUEST;


    public static HttpClientCodecSetting createDefault() {
        return new HttpClientCodecSetting();
    }

    public int getMaxInitialLineLength() {
        return maxInitialLineLength;
    }

    public HttpClientCodecSetting setMaxInitialLineLength(int maxInitialLineLength) {
        this.maxInitialLineLength = maxInitialLineLength;
        return this;
    }

    public int getMaxHeaderSize() {
        return maxHeaderSize;
    }

    public HttpClientCodecSetting setMaxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
        return this;
    }

    public int getMaxChunkSize() {
        return maxChunkSize;
    }

    public HttpClientCodecSetting setMaxChunkSize(int maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
        return this;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public HttpClientCodecSetting setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
        return this;
    }

    public boolean isDecompressorStrict() {
        return decompressorStrict;
    }

    public HttpClientCodecSetting setDecompressorStrict(boolean decompressorStrict) {
        this.decompressorStrict = decompressorStrict;
        return this;
    }

    public boolean isFailOnMissingResponse() {
        return failOnMissingResponse;
    }

    public HttpClientCodecSetting setFailOnMissingResponse(boolean failOnMissingResponse) {
        this.failOnMissingResponse = failOnMissingResponse;
        return this;
    }

    public boolean isValidateHeaders() {
        return validateHeaders;
    }

    public HttpClientCodecSetting setValidateHeaders(boolean validateHeaders) {
        this.validateHeaders = validateHeaders;
        return this;
    }

    public boolean isParseHttpAfterConnectRequest() {
        return parseHttpAfterConnectRequest;
    }

    public HttpClientCodecSetting setParseHttpAfterConnectRequest(boolean parseHttpAfterConnectRequest) {
        this.parseHttpAfterConnectRequest = parseHttpAfterConnectRequest;
        return this;
    }
}
