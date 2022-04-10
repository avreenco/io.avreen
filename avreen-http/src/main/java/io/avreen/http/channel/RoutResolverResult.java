package io.avreen.http.channel;

import java.util.Set;
import java.util.regex.Pattern;

public class RoutResolverResult {
    private String channelId;
    private String rewriteUri;
    private String endpointId;
    private Set<String> requestRemoveHeaders;
    private Set<String> responseRemoveHeaders;
    private Pattern requestRemoveHeadersPattern;
    private Pattern responseRemoveHeadersPattern;


    public RoutResolverResult(String channelId, String rewriteUri, String endpointId) {
        this.channelId = channelId;
        this.rewriteUri = rewriteUri;
        this.endpointId = endpointId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getRewriteUri() {
        return rewriteUri;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public Set<String> getRequestRemoveHeaders() {
        return requestRemoveHeaders;
    }

    public RoutResolverResult setRequestRemoveHeaders(Set<String> requestRemoveHeaders) {
        this.requestRemoveHeaders = requestRemoveHeaders;
        return this;
    }

    public Set<String> getResponseRemoveHeaders() {
        return responseRemoveHeaders;
    }

    public RoutResolverResult setResponseRemoveHeaders(Set<String> responseRemoveHeaders) {
        this.responseRemoveHeaders = responseRemoveHeaders;
        return this;
    }

    public Pattern getRequestRemoveHeadersPattern() {
        return requestRemoveHeadersPattern;
    }

    public RoutResolverResult setRequestRemoveHeadersPattern(Pattern requestRemoveHeadersPattern) {
        this.requestRemoveHeadersPattern = requestRemoveHeadersPattern;
        return this;
    }

    public Pattern getResponseRemoveHeadersPattern() {
        return responseRemoveHeadersPattern;
    }

    public RoutResolverResult setResponseRemoveHeadersPattern(Pattern responseRemoveHeadersPattern) {
        this.responseRemoveHeadersPattern = responseRemoveHeadersPattern;
        return this;
    }
}
