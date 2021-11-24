package io.avreen.iso8583.handler;

import io.avreen.common.context.MsgContext;
import io.avreen.common.netty.IMsgProcessor;
import io.avreen.iso8583.common.ISOMsg;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * The class Mti base msg processor.
 */
public class MTIBaseMsgProcessor implements IMsgProcessor<ISOMsg> {
    private Map<String, IMsgProcessor<ISOMsg>> processorMap;
    private IMsgProcessor<ISOMsg> defaultHandler;

    /**
     * Gets default handler.
     *
     * @return the default handler
     */
    public IMsgProcessor<ISOMsg> getDefaultHandler() {
        return defaultHandler;
    }

    /**
     * Sets default handler.
     *
     * @param defaultHandler the default handler
     */
    public void setDefaultHandler(IMsgProcessor<ISOMsg> defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    /**
     * Gets processor map.
     *
     * @return the processor map
     */
    public Map<String, IMsgProcessor<ISOMsg>> getProcessorMap() {
        return processorMap;
    }

    @Override
    public void process(ChannelHandlerContext channelHandlerContext, MsgContext<ISOMsg> msg) {
        IMsgProcessor<ISOMsg> msgHandler = null;
        if (processorMap != null) {
            if (processorMap.containsKey(msg.getMsg().getMTI()))
                msgHandler = processorMap.get(msg.getMsg().getMTI());
        }
        if (msgHandler == null)
            msgHandler = defaultHandler;
        if (msgHandler == null)
            throw new RuntimeException("can not found msg handler with MTI=" + msg.getMsg().getMTI());
        msgHandler.process(channelHandlerContext, msg);

    }
}
