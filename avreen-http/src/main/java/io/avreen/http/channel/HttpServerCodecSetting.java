package io.avreen.http.channel;


import io.netty.handler.codec.http.HttpObjectDecoder;

/**
 * The class Http server properties.
 */
public class HttpServerCodecSetting {

    private int maxInitialLineLength = HttpObjectDecoder.DEFAULT_MAX_INITIAL_LINE_LENGTH;
    private int maxHeaderSize = HttpObjectDecoder.DEFAULT_MAX_HEADER_SIZE;
    private int maxChunkSize = HttpObjectDecoder.DEFAULT_MAX_CHUNK_SIZE;
    private int maxContentLength = Integer.MAX_VALUE;
    private int compressorLevel = 6;
    private int compressorWindowBits = 15;
    private int compressorMemLevel = 8;
    private int compressorContentSizeThreshold = 0;

    public static HttpServerCodecSetting createDefault()
    {
        return new HttpServerCodecSetting();
    }

    public int getMaxInitialLineLength() {
        return maxInitialLineLength;
    }

    public HttpServerCodecSetting setMaxInitialLineLength(int maxInitialLineLength) {
        this.maxInitialLineLength = maxInitialLineLength;
        return this;
    }

    public int getMaxHeaderSize() {
        return maxHeaderSize;
    }

    public HttpServerCodecSetting setMaxHeaderSize(int maxHeaderSize) {
        this.maxHeaderSize = maxHeaderSize;
        return this;
    }

    public int getMaxChunkSize() {
        return maxChunkSize;
    }

    public HttpServerCodecSetting setMaxChunkSize(int maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
        return this;
    }

    public int getMaxContentLength() {
        return maxContentLength;
    }

    public HttpServerCodecSetting setMaxContentLength(int maxContentLength) {
        this.maxContentLength = maxContentLength;
        return this;
    }

    public int getCompressorLevel() {
        return compressorLevel;
    }

    public HttpServerCodecSetting setCompressorLevel(int compressorLevel) {
        this.compressorLevel = compressorLevel;
        return this;
    }

    public int getCompressorWindowBits() {
        return compressorWindowBits;
    }

    public HttpServerCodecSetting setCompressorWindowBits(int compressorWindowBits) {
        this.compressorWindowBits = compressorWindowBits;
        return this;
    }

    public int getCompressorMemLevel() {
        return compressorMemLevel;
    }

    public HttpServerCodecSetting setCompressorMemLevel(int compressorMemLevel) {
        this.compressorMemLevel = compressorMemLevel;
        return this;
    }

    public int getCompressorContentSizeThreshold() {
        return compressorContentSizeThreshold;
    }

    public HttpServerCodecSetting setCompressorContentSizeThreshold(int compressorContentSizeThreshold) {
        this.compressorContentSizeThreshold = compressorContentSizeThreshold;
        return this;
    }
}
