package io.avreen.http.logger;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import io.avreen.common.log.LoggerDomain;
import io.avreen.http.common.HttpMsg;
import io.avreen.http.common.IUriMatcher;
import io.avreen.util.ChannelLogInfo;
import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The class Slf 4 j http msg log handler.
 */
@ChannelHandler.Sharable
public class SLF4JHttpMsgLogHandler extends ChannelDuplexHandler implements Cloneable {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.channel.SLF4JHttpMsgLogHandler");

    private List<HttpMessageLogProperties> logPropertiesMap = new ArrayList<>();
    //private SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    private String name;
    private IUriMatcher uriMatcher;

    /**
     * Instantiates a new Slf 4 j http msg log handler.
     */
    public SLF4JHttpMsgLogHandler(String name, List<HttpMessageLogProperties> logPropertiesMap) {
        this.logPropertiesMap = logPropertiesMap;
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    public void setUriMatcher(IUriMatcher uriMatcher) {
        this.uriMatcher = uriMatcher;
    }

    private SLF4JHttpMsgLogger getMsgLogger(ChannelHandlerContext ctx, HttpMsg httpMsg) {
        if (httpMsg.isRequest()) {
            HttpMessageLogProperties logProp = null;
            String uri = httpMsg.getUri();
            IUriMatcher matcher = this.uriMatcher;
            if (matcher == null)
                matcher = this::match;

            for (HttpMessageLogProperties httpMessageLogProperties : logPropertiesMap) {
                if (matcher.match(httpMessageLogProperties.getUrlMatcherPattern(), uri)) {
                    logProp = httpMessageLogProperties;
                    break;
                }
            }

            if (logProp != null && !logProp.isEnable()) {
                logger.warn("not msg logger found for uri={}. because config disable value", uri);
                return null;
            }
            SLF4JHttpMsgLogger msgLogger = new SLF4JHttpMsgLogger(uri);
            msgLogger.setName(getName());
            if (logProp != null && logProp.getExcludeFields() != null) {
                HashSet<String> set = new HashSet<>();
                for (String field : logProp.getExcludeFields()) {
                    set.add(field);
                }
                msgLogger.setExcludeField(set);
            }
            if (logProp != null && logProp.getIncludeFields() != null) {
                HashSet<String> set = new HashSet<>();
                for (String field : logProp.getIncludeFields()) {
                    set.add(field);
                }
                msgLogger.setIncludeField(set);
            }
            if (logProp != null && logProp.getHeaderFields() != null) {
                HashSet<String> set = new HashSet<>();
                for (String field : logProp.getHeaderFields()) {
                    set.add(field);
                }
                msgLogger.setHeaderFields(set);
            }
            if (logProp != null) {
                msgLogger.setMessageTraceFieldDelimiter(logProp.getMessageTraceFieldDelimiter());
                msgLogger.setMessageTraceField(logProp.getMessageTraceField());
            }
            AttributeKey<SLF4JHttpMsgLogger> channelSlf4JHttpMsgLogHandler = AttributeKey.valueOf("_channel_SLF4JHttpMsgLogHandler");
            Attribute<SLF4JHttpMsgLogger> attr = ctx.channel().attr(channelSlf4JHttpMsgLogHandler);
            attr.set(msgLogger);
            return msgLogger;

        } else {
            Channel channel = ctx.channel();
            AttributeKey<SLF4JHttpMsgLogger> channelSlf4JHttpMsgLogHandler = AttributeKey.valueOf("_channel_SLF4JHttpMsgLogHandler");
            SLF4JHttpMsgLogger httpMsgLogger = null;
            if (channelSlf4JHttpMsgLogHandler != null && channel.hasAttr(channelSlf4JHttpMsgLogHandler)) {
                Attribute<SLF4JHttpMsgLogger> attrVal = channel.attr(channelSlf4JHttpMsgLogHandler);
                httpMsgLogger = attrVal.get();
            }
            if (httpMsgLogger == null)
                logger.warn("can not load channel logger from cache for key={}", ctx.channel().id());
            return httpMsgLogger;

        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof HttpMsg))
            return;
        try {
            HttpMsg httpMsg = (HttpMsg) msg;
            SLF4JHttpMsgLogger msgLogger = getMsgLogger(ctx, httpMsg);
            if (msgLogger == null)
                return;

            ChannelLogInfo channelLogInfo = null;
            if (ctx.channel() != null)
                channelLogInfo = new ChannelLogInfo(ctx.channel().remoteAddress(), ctx.channel().localAddress(), ctx.channel().id().toString());
            msgLogger.logIncoming(httpMsg, channelLogInfo);
            ctx.fireChannelRead(httpMsg);
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("log channel read event failed.", e);
        }

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        if (!(msg instanceof HttpMsg))
            return;
        try {
            HttpMsg httpMsg = (HttpMsg) msg;
            SLF4JHttpMsgLogger msgLogger = getMsgLogger(ctx, httpMsg);
            if (msgLogger == null)
                return;
            ;
            ChannelLogInfo channelLogInfo = null;
            if (ctx.channel() != null)
                channelLogInfo = new ChannelLogInfo(ctx.channel().remoteAddress(), ctx.channel().localAddress(), ctx.channel().id().toString());
            msgLogger.logOutgoing(httpMsg, channelLogInfo);
        } catch (Exception e) {
            if (logger.isErrorEnabled())
                logger.error("log channel write event failed.", e);
        }
    }


    public boolean match(String pattern, String uri) {
        return pattern.equalsIgnoreCase(uri);
    }
}