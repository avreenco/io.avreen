package io.avreen.http.common;

import io.netty.handler.codec.http.*;

import java.util.Map;

/**
 * The type Http msg builder.
 */
public class HttpMsgBuilder {
    private HttpMsg httpMsg;

    /**
     * Instantiates a new Http msg builder.
     */
    public HttpMsgBuilder() {
        httpMsg = new HttpMsg();
    }

    /**
     * Instantiates a new Http msg builder.
     *
     * @param httpMsg the http msg
     */
    public HttpMsgBuilder(HttpMsg httpMsg) {
        this.httpMsg = httpMsg;
    }

    /**
     * Uri http msg builder.
     *
     * @param uri the uri
     * @return the http msg builder
     */
    public HttpMsgBuilder uri(String uri) {
        httpMsg.setUri(uri);
        return this;
    }
    public HttpMsgBuilder endpoint(String endpointId) {
        httpMsg.setEndpointId(endpointId);
        return this;
    }

    /**
     * Content http msg builder.
     *
     * @param content the content
     * @return the http msg builder
     */
    public HttpMsgBuilder content(String content) {
        httpMsg.setContent(content);
        return this;
    }

    /**
     * Clear http msg builder.
     *
     * @return the http msg builder
     */
    public HttpMsgBuilder clear() {
        httpMsg.setContent(null);
        httpMsg.setUri(null);
        httpMsg.getHeaders().clear();
        return this;
    }


    /**
     * Content type json http msg builder.
     *
     * @return the http msg builder
     */
    public HttpMsgBuilder contentTypeJson() {
        httpMsg.getHeaders().put(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString());
        return this;
    }

    /**
     * Put all http msg builder.
     *
     * @param allHeaders the all headers
     * @return the http msg builder
     */
    public HttpMsgBuilder putAll(Map<String,String> allHeaders) {
        httpMsg.getHeaders().putAll(allHeaders);
        return this;
    }

    /**
     * Content type xml http msg builder.
     *
     * @return the http msg builder
     */
    public HttpMsgBuilder contentTypeXml() {
        httpMsg.getHeaders().put(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_XML.toString());
        return this;
    }

    /**
     * Content type text http msg builder.
     *
     * @return the http msg builder
     */
    public HttpMsgBuilder contentTypeText() {
        httpMsg.getHeaders().put(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.TEXT_PLAIN.toString());
        return this;
    }

    /**
     * Host http msg builder.
     *
     * @param hostName the host name
     * @return the http msg builder
     */
    public HttpMsgBuilder host(String  hostName) {
        httpMsg.getHeaders().put(HttpHeaderNames.HOST.toString(), hostName);
        return this;
    }

    /**
     * Post http msg builder.
     *
     * @return the http msg builder
     */
    public HttpMsgBuilder post() {
        httpMsg.setMethod(HttpMethod.POST.name());
        return this;
    }

    /**
     * Version 1 1 http msg builder.
     *
     * @return the http msg builder
     */
    public HttpMsgBuilder version_1_1() {
        httpMsg.setVersion(HttpVersion.HTTP_1_1.text());
        return this;
    }

    /**
     * Version 1 0 http msg builder.
     *
     * @return the http msg builder
     */
    public HttpMsgBuilder version_1_0() {
        httpMsg.setVersion(HttpVersion.HTTP_1_0.text());
        return this;
    }

    /**
     * Method http msg builder.
     *
     * @param httpMethod the http method
     * @return the http msg builder
     */
    public HttpMsgBuilder method(HttpMethod httpMethod) {
        httpMsg.setMethod(httpMethod.name());
        return this;
    }

    /**
     * Status http msg builder.
     *
     * @param status the status
     * @return the http msg builder
     */
    public HttpMsgBuilder status(HttpResponseStatus status) {
        httpMsg.setStatus(status.code());
        return this;
    }

    /**
     * Get http msg builder.
     *
     * @return the http msg builder
     */
    public HttpMsgBuilder get() {
        httpMsg.setMethod(HttpMethod.GET.name());
        return this;
    }

    /**
     * Add header http msg builder.
     *
     * @param headerNames  the header names
     * @param headerValues the header values
     * @return the http msg builder
     */
    public HttpMsgBuilder addHeader(HttpHeaderNames headerNames, HttpHeaderValues headerValues) {
        httpMsg.getHeaders().put(headerNames.toString(), headerValues.toString());
        return this;
    }
    public HttpMsgBuilder addHeader(String  headerName , String  headerValue) {
        httpMsg.getHeaders().put(headerName, headerValue);
        return this;
    }

    /**
     * Build request http msg.
     *
     * @return the http msg
     */
    public HttpMsg buildRequest() {
        httpMsg.setRequest(true);
        return httpMsg;
    }

    /**
     * Build response http msg.
     *
     * @return the http msg
     */
    public HttpMsg buildResponse() {
        httpMsg.setRequest(false);
        return httpMsg;
    }


}
