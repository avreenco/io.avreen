package io.avreen.hb;

import io.avreen.common.context.MsgContext;
import io.avreen.common.netty.IMsgProcessor;
import io.avreen.common.util.CodecUtil;
import io.avreen.common.util.TPDUUtil;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * The class Tpdu base msg processor.
 */
public class TPDUBaseMsgProcessor implements IMsgProcessor<HeaderBodyObject> {
    private Map<String, IMsgProcessor<HeaderBodyObject>> processMap;

    /**
     * Gets process map.
     *
     * @return the process map
     */
    public Map<String, IMsgProcessor<HeaderBodyObject>> getProcessMap() {
        return processMap;
    }

    /**
     * Sets process map.
     *
     * @param processMap the process map
     */
    public void setProcessMap(Map<String, IMsgProcessor<HeaderBodyObject>> processMap) {
        this.processMap = processMap;
    }


    @Override
    public void process(ChannelHandlerContext channelHandlerContext, MsgContext<HeaderBodyObject> msg) {
        HeaderBodyObject headerBodyObject = msg.getMsg();
        byte[] bytes = TPDUUtil.getDestinationAddress(headerBodyObject.getHeader());
        String hexString = CodecUtil.hexString(bytes);
        hexString = CodecUtil.padleft(hexString, 4, '0');
        IMsgProcessor<HeaderBodyObject> msgHandler = null;
        if (processMap != null) {
            if (processMap.containsKey(hexString))
                msgHandler = processMap.get(hexString);
        }
        if (msgHandler == null)
            throw new RuntimeException("can not found msg handler with Origin=" + hexString);
        msgHandler.process(channelHandlerContext, msg);

    }
}
