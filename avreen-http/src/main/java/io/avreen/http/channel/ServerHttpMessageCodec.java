package io.avreen.http.channel;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.context.ContextKeyUtil;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.context.NodeInfo;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.netty.IMessageCodecListener;
import io.avreen.http.MethodNotAllowException;
import io.avreen.http.common.HttpMsg;
import io.avreen.http.common.HttpRequestMsg;
import io.avreen.http.common.HttpResponseMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

import static io.netty.buffer.Unpooled.copiedBuffer;


/**
 * The type Server http message codec.
 */
@ChannelHandler.Sharable
public class ServerHttpMessageCodec extends HttpMessageCodec {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.channel.ServerHttpMessageCodec");
    private static final String EMPTY_STRING = "";
    private IHttpRoutResolver routResolver;
    private Set<String> traceAbleHeaderNames;
    private static Set<String> defaultTraceAbleHeaderNames = new HashSet<>();

    static {
        defaultTraceAbleHeaderNames.add(HttpHeaderNames.AUTHORIZATION.toString().toUpperCase());
    }

    /**
     * Instantiates a new Server http message codec.
     *
     * @param channelId      the channel id
     * @param codecListener1 the codec listener 1
     */
    public ServerHttpMessageCodec(String channelId, IMessageCodecListener<HttpMsg> codecListener1, IHttpRoutResolver routResolver, Set<String> traceAbleHeaderNames) {
        super(channelId, codecListener1);
        this.routResolver = routResolver;
        if (traceAbleHeaderNames != null) {
            this.traceAbleHeaderNames = new HashSet<>();
            for (String traceAbleHeaderName : traceAbleHeaderNames) {
                this.traceAbleHeaderNames.add(traceAbleHeaderName.toUpperCase());
            }
        } else {
            this.traceAbleHeaderNames = defaultTraceAbleHeaderNames;
        }
    }


    private HttpMessage handleResponseOut(HttpMsg httpMsg, Set<String> removeHeaders) throws Exception {
        HttpMessage httpMessage = null;
        Integer httpStatusCode = httpMsg.getStatus();
        HttpResponseStatus responseStatus;
        Map<String, Object> initialHeaders = new HashMap<>();
        if (httpStatusCode == null || httpStatusCode == HttpResponseStatus.OK.code())
            responseStatus = HttpResponseStatus.OK;
        else
            responseStatus = HttpResponseStatus.valueOf(httpStatusCode);
        String messageBody;
        messageBody = httpMsg.getContent();

        if (responseStatus == HttpResponseStatus.UNAUTHORIZED ||
                responseStatus == HttpResponseStatus.FORBIDDEN) {
            initialHeaders.put("WWW-Authenticate", "Basic realm=\"Avreen  Services\"");
        }
        if (messageBody == null)
            messageBody = EMPTY_STRING;

        byte[] allBytes = messageBody.getBytes(StandardCharsets.UTF_8);
        httpMessage = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                responseStatus,
                copiedBuffer(allBytes)
        );
        for (String key : initialHeaders.keySet()) {
            httpMessage.headers().add(key, initialHeaders.get(key));
        }

        httpMessage.headers().set(HttpHeaderNames.CONTENT_TYPE,
                HttpHeaderValues.APPLICATION_JSON);
        Map<String, String> sendHeader = httpMsg.getHeaders();
        for (String key : sendHeader.keySet()) {
            if (removeHeaders != null && removeHeaders.contains(key))
                continue;
            httpMessage.headers().set(key, sendHeader.get(key));
        }


        httpMessage.headers().set(HttpHeaderNames.CONTENT_ENCODING,
                "utf-8");

        httpMessage.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                allBytes.length);
        if (logger.isDebugEnabled())
            logger.debug("response out content={}", messageBody);

        return httpMessage;
    }

    private Set<String> getRemoveHeaders(RoutResolverResult routResolverResult, boolean isRequest, Set<String> headers) {
        HashSet<String> removeHeadersSet = null;
        if (routResolverResult != null) {
            removeHeadersSet = new HashSet<>();
            {
                Set<String> removeSet = null;
                if (isRequest)
                    removeSet = routResolverResult.getRequestRemoveHeaders();
                else
                    removeSet = routResolverResult.getResponseRemoveHeaders();
                if (removeSet != null) {
                    for (String headerName : removeSet) {
                        removeHeadersSet.add(headerName);
                    }
                }
            }
            Pattern pattern = null;
            if (isRequest)
                pattern = routResolverResult.getRequestRemoveHeadersPattern();
            else
                pattern = routResolverResult.getResponseRemoveHeadersPattern();
            if (pattern != null) {
                for (String header : headers) {
                    if (pattern.matcher(header).find()) {
                        removeHeadersSet.add(header);
                    }
                }
            }
        }
        return removeHeadersSet;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> list) throws Exception {
        HttpMessage httpMessage = null;
        HttpMsg httpMsg = null;
        Exception exception = null;
        if (o instanceof HttpMsg) {
            try {
                httpMsg = (HttpMsg) o;
                MsgTracer.current().inject();

                if (codecListener != null)
                    codecListener.beforeEncode(channelHandlerContext, httpMsg);
                Channel channel = channelHandlerContext.channel();
                Set<String> removeHeaders = null;
                AttributeKey<RoutResolverResult> channel_rout_setting = AttributeKey.valueOf("_channel_rout_setting");
                if (channel_rout_setting != null && channel.hasAttr(channel_rout_setting)) {
                    Attribute<RoutResolverResult> attr = channel.attr(channel_rout_setting);
                    if (attr != null) {
                        RoutResolverResult routResolverResult = attr.get();
                        if (routResolverResult != null) {
                            removeHeaders = getRemoveHeaders(routResolverResult, false, httpMsg.getHeaders().keySet());
                        }
                    }
                }
                if (!httpMsg.isRequest() || httpMsg.isReject()) {
                    httpMessage = handleResponseOut(httpMsg, removeHeaders);
                } else {
                    throw new RuntimeException("server mode not allow send(encode) request");
                }
                if (logger.isDebugEnabled())
                    logger.debug("http encode message={}", httpMessage);
                list.add(httpMessage);

            } catch (Exception ex) {
                if (logger.isErrorEnabled())
                    logger.error("encode error", ex);
                exception = ex;
            } finally {
                if (codecListener != null)
                    codecListener.afterEncode(channelHandlerContext, httpMsg, exception);

            }

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        super.channelReadComplete(ctx);
    }

    private void initMsgTracer(HttpMessage httpMessage, MsgTracer msgTracer) {

        HttpHeaders headers = httpMessage.headers();
        if (traceAbleHeaderNames != null && headers != null) {
            for (Map.Entry<String, String> header : headers) {
                String headerKey = header.getKey();
                if (traceAbleHeaderNames.contains(headerKey.toUpperCase())) {
                    msgTracer.put("H:_" + headerKey.toUpperCase(), header.getValue());
                }
            }
        }
        if (headers != null && headers.contains("X-BTN")) {
            msgTracer.put("X-BTN", headers.get("X-BTN"));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        processException(ctx, cause);
    }

    private void processException1(ChannelHandlerContext channelHandlerContext, Throwable ex) {
        if (logger.isErrorEnabled())
            logger.error("exception in", ex);
        ByteBuf content;
        String messageBody;
        HttpResponseStatus responseStatus;
        if (ex instanceof MethodNotAllowException) {
            responseStatus = HttpResponseStatus.METHOD_NOT_ALLOWED;
            messageBody = ex.getMessage();
        }
        else   if (ex instanceof TimeoutException) {
            responseStatus = HttpResponseStatus.GATEWAY_TIMEOUT;
            messageBody = "Read Timeout";
        } else {
            responseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR;
            messageBody = ex.getClass().getSimpleName();

        }
        byte[] allBytes = messageBody.getBytes(StandardCharsets.UTF_8);
        content = copiedBuffer(allBytes);
        DefaultHttpResponse defaultHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, responseStatus, content);
        defaultHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,
                HttpHeaderValues.TEXT_PLAIN);
        defaultHttpResponse.headers().set(HttpHeaderNames.CONTENT_ENCODING,
                "utf-8");

        defaultHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                allBytes.length);

        channelHandlerContext.writeAndFlush(defaultHttpResponse);

    }
    private void processException(ChannelHandlerContext channelHandlerContext, Throwable ex) {
        if (logger.isErrorEnabled())
            logger.error("exception in", ex);
        ByteBuf content;
        String messageBody;
        HttpMsg httpMsg = new HttpResponseMsg();
        HttpResponseStatus responseStatus;
        if (ex instanceof MethodNotAllowException) {
            responseStatus = HttpResponseStatus.METHOD_NOT_ALLOWED;
            messageBody = ex.getMessage();
        }
        else   if (ex instanceof TimeoutException) {
            responseStatus = HttpResponseStatus.GATEWAY_TIMEOUT;
            messageBody = "Read Timeout";
        } else {
            responseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR;
            messageBody = ex.getClass().getSimpleName();

        }
        httpMsg.getHeaders().put(HttpHeaderNames.CONTENT_TYPE.toString(),
                HttpHeaderValues.TEXT_PLAIN.toString());
        httpMsg.getHeaders().put(HttpHeaderNames.CONTENT_ENCODING.toString(),
                "utf-8");
        httpMsg.setStatus(responseStatus.code());
        httpMsg.setContent(messageBody);

        channelHandlerContext.channel().writeAndFlush(httpMsg);

    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> list) throws Exception {
        MsgTracer msgTracer = new MsgTracer();
        if (channelId != null)
            msgTracer.put("ChannelId", channelId);
        msgTracer.put("EdgeNodeId", Integer.toString(NodeInfo.NODE_ID));

        if (!(o instanceof HttpMessage)) {
            if (logger.isErrorEnabled())
                logger.error("incoming message is not FullHttpRequest");
            return;
        }
        Exception exception = null;
        if (codecListener != null)
            codecListener.beforeDecode(channelHandlerContext);
        final HttpMessage httpMessage = (HttpMessage) o;

        initMsgTracer(httpMessage, msgTracer);
        HttpRequestMsg httpRequestMsg = new HttpRequestMsg();
        String applyChannelId = channelId;
        try {
            String content = null;
            if (httpMessage instanceof ByteBufHolder)
                content = ((ByteBufHolder) httpMessage).content().toString(CharsetUtil.UTF_8);
            if (logger.isDebugEnabled()) {
                logger.debug("http decode request message={} body={}", o, content);
            }
            String real_ip = null;
            if (httpMessage instanceof HttpRequest) {
                httpRequestMsg.setContent(content);
                httpRequestMsg.setRequest(true);
                Channel inChannel = channelHandlerContext.channel();
                httpRequestMsg.setRemoteAddress((InetSocketAddress) inChannel.remoteAddress());
                String request_uri = ((HttpRequest) httpMessage).uri();
                httpRequestMsg.setUri(request_uri);
                httpRequestMsg.setVersion(httpMessage.protocolVersion().text());
                String forwarder_for = null;
                List<Map.Entry<String, String>> _headers = httpMessage.headers().entries();
                for (Map.Entry<String, String> entry : _headers) {
                    String headerKey = entry.getKey();
                    if (forwarder_for == null && headerKey.equalsIgnoreCase("X-FORWARDED-FOR")) {
                        forwarder_for = entry.getValue();
                    }
                    httpRequestMsg.getHeaders().put(headerKey, entry.getValue());
                }
                if (forwarder_for != null) {
                    String[] split = forwarder_for.split(",");
                    real_ip = split[0];
                }
                if (real_ip == null)
                    real_ip = httpRequestMsg.getRemoteAddress().getHostString();
                if (real_ip != null)
                    msgTracer.put("REAL-IP", real_ip);

                channelHandlerContext.fireChannelRead(httpRequestMsg);

                if (routResolver != null) {
                    RoutResolverResult resolve = routResolver.resolve(request_uri);
                    if (resolve != null) {
                        if (resolve.getRewriteUri() != null)
                            httpRequestMsg.setUri(resolve.getRewriteUri());
                        if (resolve.getChannelId() != null) {
                            applyChannelId = resolve.getChannelId();
                            msgTracer.put("ChannelId", applyChannelId);
                        }
                        String endpointId = resolve.getEndpointId();
                        httpRequestMsg.setEndpointId(endpointId);
                        AttributeKey<RoutResolverResult> rout_setting = AttributeKey.valueOf("_channel_rout_setting");
                        Attribute<RoutResolverResult> attr = channelHandlerContext.channel().attr(rout_setting);
                        attr.set(resolve);
                        Set<String> requestRemoveHeaders = getRemoveHeaders(resolve, true, httpMessage.headers().names());
                        if (requestRemoveHeaders != null) {
                            for (String requestRemoveHeader : requestRemoveHeaders) {
                                httpMessage.headers().remove(requestRemoveHeader);
                            }
                        }
                    }
                }
            } else if (httpMessage instanceof HttpResponse) {
                throw new RuntimeException("server mode not allow decode HttpResponse");
            } else
                throw new RuntimeException("http message type not detected");

        } catch (Exception ex) {
            //channelHandlerContext.fireChannelRead(httpMessage);
            processException(channelHandlerContext, ex);
            exception = ex;
        } finally {
            if (codecListener != null && httpRequestMsg != null && exception == null) {
                Channel channel = channelHandlerContext.channel();
                if (channel.isOpen() && channel.isWritable() && channel.isActive()) {
                    Map<String, Object> additionalInfo = new HashMap<>();
                    additionalInfo.put(ContextKeyUtil.MSG_TRACE, msgTracer);
                    codecListener.afterDecode(channelHandlerContext, applyChannelId, httpRequestMsg, exception, additionalInfo);
                }
            }

        }

    }


}

