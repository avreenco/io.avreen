package io.avreen.http.bomber;

/**
 * Created by asgharnejad on 9/16/2017.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.avreen.common.cache.CacheBaseRateLimiter;
import io.avreen.common.cache.CacheManagerRegistry;
import io.avreen.common.cache.ICacheManager;
import io.avreen.common.cache.SimpleCacheManager;
import io.avreen.common.log.LoggerDomain;
import io.avreen.http.common.HttpMsg;
import io.avreen.http.common.IUriMatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class Slf 4 j http msg log handler.
 */
@ChannelHandler.Sharable
public class HttpMsgBomberControllerHandler extends ChannelInboundHandlerAdapter implements Cloneable {

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".http.bomber.HttpMsgBomberControllerHandler");

    private List<HttpMsgBomberProperties> bomberProperties;
    private String name;
    private IUriMatcher uriMatcher;
    private ConcurrentHashMap<String, CacheBaseRateLimiter> baseRateLimiterMap = new ConcurrentHashMap<>();
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Instantiates a new Slf 4 j http msg log handler.
     */
    public HttpMsgBomberControllerHandler(String name, List<HttpMsgBomberProperties> bomberProperties) {
        this.bomberProperties = bomberProperties;
        this.name = name;
    }

    private CacheBaseRateLimiter getCacheBaseRateLimiter(HttpMsgBomberProperties bomberProperties) {
        String key = bomberProperties.getId();
        if (key == null)
            key = bomberProperties.getUrlMatcherPattern();
        if (baseRateLimiterMap.containsKey(key))
            return baseRateLimiterMap.get(key);
        synchronized (this) {
            if (baseRateLimiterMap.containsKey(key))
                return baseRateLimiterMap.get(key);
            ICacheManager cacheManager;
            String cacheManagerName = bomberProperties.getCacheManagerName();
            if (cacheManagerName != null)
                cacheManager = CacheManagerRegistry.getCacheManager(cacheManagerName);
            else
                cacheManager = new SimpleCacheManager();
            CacheBaseRateLimiter cacheBaseRateLimiter = new CacheBaseRateLimiter(cacheManager);
            baseRateLimiterMap.put(key, cacheBaseRateLimiter);
            return cacheBaseRateLimiter;
        }
    }

    private String getBomberKey(HttpMsg httpMsg, HttpMsgBomberProperties bomberProperties) {
        Map map = null;
        try {
            map = objectMapper.readValue(httpMsg.getContent(), HashMap.class);
        } catch (JsonProcessingException e) {
            logger.warn("bomber key detect error", e);
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (bomberProperties.getBomberFields() != null) {
            for (String bomberField : bomberProperties.getBomberFields()) {
                if (map.containsKey(bomberField)) {
                    stringBuilder.append(map.get(bomberField));
                    stringBuilder.append(bomberProperties.getBomberFieldsDelimiter());
                }
            }
        }
        String s = stringBuilder.toString();
        if (s.isEmpty())
            return bomberProperties.getUrlMatcherPattern();
        return s;
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

    private boolean checkMsgLogger(ChannelHandlerContext ctx, HttpMsg httpMsg) {
        if (!httpMsg.isRequest() || httpMsg.isReject()) {
//            Channel channel = ctx.channel();
//            AttributeKey<BomberCacheEntry> channelSlf4JHttpMsgLogHandler = AttributeKey.valueOf("_channel_CacheBaseRateLimiter");
//            BomberCacheEntry bomberCacheEntry = null;
//            if (channelSlf4JHttpMsgLogHandler != null && channel.hasAttr(channelSlf4JHttpMsgLogHandler)) {
//                Attribute<BomberCacheEntry> attrVal = channel.attr(channelSlf4JHttpMsgLogHandler);
//                bomberCacheEntry = attrVal.get();
//            }
//            if (bomberCacheEntry == null)
//                logger.warn("can not load channel CacheBaseRateLimiter from cache for key={}", ctx.channel().id());
//            CacheBaseRateLimiter cacheBaseRateLimiter = bomberCacheEntry.getCacheBaseRateLimiter();
//            cacheBaseRateLimiter.tick(bomberCacheEntry.getKey() , bomberCacheEntry.getBomberProperties().getCacheTimeout().toMillis());
            return true;
        }
        HttpMsgBomberProperties bomberProperties = null;
        String uri = httpMsg.getUri();
        IUriMatcher matcher = this.uriMatcher;
        if (matcher == null)
            matcher = this::match;

        for (HttpMsgBomberProperties msgBomberProperties : this.bomberProperties) {
            if (matcher.match(msgBomberProperties.getUrlMatcherPattern(), uri)) {
                bomberProperties = msgBomberProperties;
                break;
            }
        }
        if (bomberProperties == null)
            return true;
        if (bomberProperties != null && !bomberProperties.isEnable()) {
            logger.warn(" found for bomber checker for bu ignored uri={}. because config disable value", uri);
            return true;
        }
        String key = getBomberKey(httpMsg, bomberProperties);
        if (key == null) {
            logger.warn(" bomber key not build url={}. bomber ignored", uri);
            return true;
        }

        CacheBaseRateLimiter cacheBaseRateLimiter = getCacheBaseRateLimiter(bomberProperties);

//        AttributeKey<BomberCacheEntry> channelBomberProperties = AttributeKey.valueOf("_channel_CacheBaseRateLimiter");
//        Attribute<BomberCacheEntry> attr = ctx.channel().attr(channelBomberProperties);
//        attr.set(new BomberCacheEntry(key ,cacheBaseRateLimiter,bomberProperties ));

        boolean check = cacheBaseRateLimiter.check(key, bomberProperties.getCallDuration().toMillis());
        if (!check)
            return false;
        cacheBaseRateLimiter.tick(key, bomberProperties.getCacheTimeout().toMillis());
        return true;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof HttpMsg))
            return;
        boolean b = checkMsgLogger(ctx, (HttpMsg) msg);
        if (!b) {
            HttpMsg httpMsg = new HttpMsg();
            httpMsg.setResponse();
            httpMsg.setStatus(HttpResponseStatus.TOO_MANY_REQUESTS.code());
            ctx.channel().writeAndFlush(httpMsg);
            ctx.channel().close();
//            for (Map.Entry<String, ChannelHandler> stringChannelHandlerEntry : ctx.pipeline()) {
//                ctx.pipeline().remove(stringChannelHandlerEntry.getValue());
//            }
        }
        ctx.fireChannelRead(msg);
    }

    public boolean match(String pattern, String uri) {
        return pattern.equalsIgnoreCase(uri);
    }
}