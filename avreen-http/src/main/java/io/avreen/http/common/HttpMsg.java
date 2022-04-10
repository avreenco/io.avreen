package io.avreen.http.common;

import io.avreen.common.context.IMsgContextAware;
import io.avreen.common.context.IRejectSupportObject;
import io.avreen.common.context.MessageTypes;
import io.avreen.common.context.MsgContext;
import io.avreen.common.netty.IMessageTypeSupplier;
import io.avreen.common.util.SimpleToStringUtil;
import io.netty.handler.codec.http.HttpVersion;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The type Http msg.
 */
public class HttpMsg implements IRejectSupportObject,
        IMsgContextAware<HttpMsg>,
        IMessageTypeSupplier, Cloneable {
    private String content;
    private boolean request;
    private Map<String, String> headers = new HashMap<>();
    private String uri;
    private String endpointId;
    private Integer status;
    private Integer rejectCode;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;
    private String version = HttpVersion.HTTP_1_1.text();
    private String method = "POST";

    @Override
    public void setMsgContext(MsgContext<HttpMsg> msgContext) {
        this.msgContext = msgContext;
    }

    @Override
    public MsgContext<HttpMsg> getMsgContext() {
        return msgContext;
    }

    @Override
    public MessageTypes getMessageTypes() {
        HttpMsg httpMsg = this;
        if (httpMsg.isReject())
            return MessageTypes.Reject;
        if (httpMsg.isRequest())
            return MessageTypes.Request;
        return MessageTypes.Response;
    }

    @Override
    public void setResponse() {
        setRequest(false);
    }

    /**
     * The enum Direction.
     */
    public enum Direction {
        /**
         * Incoming direction.
         */
        incoming,
        /**
         * Outgoing direction.
         */
        outgoing;

    }

    private transient MsgContext<HttpMsg> msgContext;

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Is request boolean.
     *
     * @return the boolean
     */
    public boolean isRequest() {
        return request;
    }

    /**
     * Sets request.
     *
     * @param request the request
     */
    public void setRequest(boolean request) {
        this.request = request;
    }

    /**
     * Gets headers.
     *
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }


    public String getIgnoreCaseHeaderValue(String httpHeaderNames) {
        Set<String> strings = getHeaders().keySet();
        for (String string : strings) {
            if (string.equalsIgnoreCase(httpHeaderNames))
                return getHeaders().get(string);
        }
        return null;
    }


    /**
     * Gets uri.
     *
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets uri.
     *
     * @param uri the uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean isReject() {
        return rejectCode != null;
    }

    @Override
    public Integer getRejectCode() {
        return rejectCode;
    }

    @Override
    public void setRejectCode(int rejectCode) {
        this.rejectCode = rejectCode;
    }

    /**
     * Gets local address.
     *
     * @return the local address
     */
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    /**
     * Sets local address.
     *
     * @param localAddress the local address
     */
    public void setLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    /**
     * Gets remote address.
     *
     * @return the remote address
     */
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Sets remote address.
     *
     * @param remoteAddress the remote address
     */
    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Is ok response boolean.
     *
     * @return the boolean
     */
    public boolean isOkResponse() {
        if (isReject() || isRequest())
            throw new RuntimeException("not allow isOkResponse");
        return (status == null || status == 200);
    }

    /**
     * Gets method.
     *
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets method.
     *
     * @param method the method
     * @return the method
     */
    public HttpMsg setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public HttpMsg setEndpointId(String endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    @Override
    public String toString() {
        return SimpleToStringUtil.toString(this);
    }

    @Override
    public HttpMsg clone() {
        HttpMsg httpMsg = new HttpMsg();
        httpMsg.content = content;
        httpMsg.request = request;
        httpMsg.uri = uri;
        httpMsg.endpointId = endpointId;
        httpMsg.status = status;
        httpMsg.rejectCode = rejectCode;
        httpMsg.localAddress = localAddress;
        httpMsg.remoteAddress = remoteAddress;
        httpMsg.version = version;
        httpMsg.method = method;
        Map<String, String> clone_headers = new HashMap<>();
        clone_headers.putAll(this.headers);
        httpMsg.headers = clone_headers;
        return httpMsg;
    }
}
