package io.avreen.mq.redis;

import io.avreen.common.context.MsgContext;
import io.avreen.common.context.MsgTracer;
import io.avreen.common.log.LoggerDomain;
import io.avreen.common.util.CodecUtil;
import io.avreen.mq.api.IMsgConsumer;
import io.avreen.serializer.ObjectSerializer;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * The class Redis listener.
 *
 * @param <M> the type parameter
 */
public class RedisListener<M> {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".reactor.messagebroker.impl.redis.RedisListener");
    private IMsgConsumer msgConsumer;
    /**
     * Instantiates a new Redis listener.
     *
     * @param msgConsumer the msg consumer
     */
    public RedisListener(IMsgConsumer msgConsumer) {
        this.msgConsumer = msgConsumer;
    }


    /**
     * Handle.
     *
     * @param qname the qname
     * @param bytes the bytes
     */
    public void handle(String qname, byte[] bytes) {
        MsgTracer msgTracer = null;
        try {
            MsgContext<M> msg = ObjectSerializer.current().readObject(bytes, MsgContext.class);
            msgTracer = msg.getTracer();
            MsgTracer.inject(msgTracer);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("receive msg for handle. queue={}  msg={}", qname, msg.getMsg());
            if(msg.expired())
            {
                LOGGER.error("!!!!!!!!!!! receive msg for handle. but expired queue={} expire time={}  msg={}", qname,msg.getExpireTime(),  msg.getMsg());
                return;
            }
            msgConsumer.onMsg(msg);
        } catch (Throwable e) {
            if (LOGGER.isErrorEnabled())
                LOGGER.error("fail process message queue name={} buffer={}", qname, CodecUtil.hexString(bytes), e);
        } finally {
            MsgTracer.eject(msgTracer);
        }
    }

}
