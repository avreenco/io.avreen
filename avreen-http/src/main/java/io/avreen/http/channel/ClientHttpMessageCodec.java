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
import io.avreen.http.common.HttpResponseMsg;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.buffer.Unpooled.copiedBuffer;


/**
 * The type Client http message codec.
 */
@ChannelHandler.Sharable
public class ClientHttpMessageCodec extends HttpMessageCodec {

    private static InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.channel.ClientHttpMessageCodec");

    /**
     * Instantiates a new Client http message codec.
     *
     * @param channelId     the channel id
     * @param codecListener the codec listener
     */
    public ClientHttpMessageCodec(String channelId, IMessageCodecListener<HttpMsg> codecListener) {
        super(channelId, codecListener);
    }

    private HttpMessage handleRequestOut(HttpMsg httpMsg) throws Exception {
        DefaultFullHttpRequest defaultFullHttpRequest = null;
        String content = httpMsg.getContent();
        if (logger.isDebugEnabled())
            logger.debug("request out content={}", content);
        byte[] allBytes = content.getBytes(StandardCharsets.UTF_8);
        HttpMethod method = HttpMethod.POST;
        if (httpMsg.getMethod() != null)
            method = HttpMethod.valueOf(httpMsg.getMethod());
        HttpVersion httpVersion = HttpVersion.HTTP_1_1;
        if (httpMsg.getVersion() != null)
            httpVersion = HttpVersion.valueOf(httpMsg.getVersion());
        String uri = httpMsg.getUri();
        if (uri == null) {
            throw new MethodNotAllowException("can not found uri to send message. endpointid=" + httpMsg.getEndpointId());
        }

        defaultFullHttpRequest = new DefaultFullHttpRequest(
                httpVersion,
                method,
                uri,
                copiedBuffer(allBytes)
        );
        defaultFullHttpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, allBytes.length);
        if (httpMsg.getHeaders() != null) {
            for (String hdr : httpMsg.getHeaders().keySet()) {
                defaultFullHttpRequest.headers().set(hdr, httpMsg.getHeaders().get(hdr));
            }
        }

        return defaultFullHttpRequest;
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> list) throws Exception {

        HttpMessage httpMessage = null;
        HttpMsg httpMsg = null;
        Exception exception = null;
        if (o instanceof HttpMsg) {
            try {
                httpMsg = (HttpMsg) o;
                MsgTracer current = MsgTracer.current();
                if (current != null)
                    current.inject();

                if (codecListener != null)
                    codecListener.beforeEncode(channelHandlerContext, httpMsg);
                if (!httpMsg.isRequest()) {
                    throw new RuntimeException("client mode not allow send(encode) not request message");
                } else {
                    httpMessage = handleRequestOut(httpMsg);
                }

                Map<String, String> sendHedaer = httpMsg.getHeaders();
                for (String key : sendHedaer.keySet()) {
                    httpMessage.headers().set(key, sendHedaer.get(key));
                }
                if (!httpMessage.headers().contains(HttpHeaderNames.CONTENT_TYPE)) {
                    httpMessage.headers().set(HttpHeaderNames.CONTENT_TYPE,
                            HttpHeaderValues.APPLICATION_JSON);
                }
                if (!httpMessage.headers().contains(HttpHeaderNames.ACCEPT)) {
                    httpMessage.headers().set(HttpHeaderNames.ACCEPT,
                            HttpHeaderValues.APPLICATION_JSON);
                }
                if (!httpMessage.headers().contains(HttpHeaderNames.HOST)) {
                    Channel channel = channelHandlerContext.channel();
                    SocketAddress socketAddress = channel.remoteAddress();
                    String hostName = HttpUtil.formatHostnameForHttp((InetSocketAddress) socketAddress);
                    httpMessage.headers().set(HttpHeaderNames.HOST, hostName);
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
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List<Object> list) throws Exception {
        MsgTracer msgTracer = new MsgTracer();
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

        HttpResponseMsg httpResponseMsg = new HttpResponseMsg();

        try {
            String content = null;
            if (httpMessage instanceof ByteBufHolder)
                content = ((ByteBufHolder) httpMessage).content().toString(CharsetUtil.UTF_8);
            if (logger.isDebugEnabled()) {
                logger.debug("http decode request message={} body={}", o, content);
            }

            if (httpMessage instanceof HttpRequest) {
                throw new RuntimeException("client mode not allow decode HttpRequest");
            } else if (httpMessage instanceof HttpResponse) {
                HttpResponse httpResponse = (HttpResponse) httpMessage;
                for (Map.Entry<String, String> entry : httpMessage.headers().entries()) {
                    httpResponseMsg.getHeaders().put(entry.getKey(), entry.getValue());
                }
                httpResponseMsg.setStatus(httpResponse.status().code());
                if (httpResponse.status().equals(HttpResponseStatus.OK)) {
                    if (content == null || content.isEmpty()) {
                        if (logger.isWarnEnabled())
                            logger.warn("receive ack from channel={}. ignore message", channelHandlerContext.channel());
                        return;
                    }
                    httpResponseMsg.setContent(content);
                } else {
                    if (logger.isWarnEnabled())
                        logger.warn("receive not approve status message. response={} channel={}", httpMessage, channelHandlerContext.channel());
                    httpResponseMsg.setContent(content);
                }
            } else
                throw new RuntimeException("http message type not detected");
            channelHandlerContext.fireChannelRead(httpResponseMsg);

        } catch (Exception ex) {
            channelHandlerContext.fireChannelRead(httpMessage);
            if (logger.isErrorEnabled())
                logger.error("exception in", ex);
            exception = ex;
        } finally {
            if (codecListener != null && httpResponseMsg != null && exception == null) {
                Map<String, Object> additionalInfo = new HashMap<>();
                additionalInfo.put(ContextKeyUtil.MSG_TRACE, msgTracer);
                codecListener.afterDecode(channelHandlerContext, channelId, httpResponseMsg, exception, additionalInfo);
            }

        }

    }


}

