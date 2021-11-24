package io.avreen.iso8583.handler;

import io.avreen.common.context.MsgContext;
import io.avreen.common.netty.IMsgProcessor;
import io.avreen.iso8583.common.ISOMsg;
import io.netty.channel.ChannelHandlerContext;

/**
 * The class Request advice msg processor.
 */
public class RequestAdviceMsgProcessor implements IMsgProcessor<ISOMsg> {
    private IMsgProcessor<ISOMsg> requestMsgProcessor;
    private IMsgProcessor<ISOMsg> adviceMsgProcessor;

    /**
     * Is advice boolean.
     *
     * @param msg the msg
     * @return the boolean
     */
    public boolean isAdvice(ISOMsg msg) {
        String mti = msg.getMTI();
        return Character.getNumericValue(mti.charAt(3)) == '2';
    }

    /**
     * Gets request msg processor.
     *
     * @return the request msg processor
     */
    public IMsgProcessor<ISOMsg> getRequestMsgProcessor() {
        return requestMsgProcessor;
    }

    /**
     * Sets request msg processor.
     *
     * @param requestMsgProcessor the request msg processor
     */
    public void setRequestMsgProcessor(IMsgProcessor<ISOMsg> requestMsgProcessor) {
        this.requestMsgProcessor = requestMsgProcessor;
    }

    /**
     * Gets advice msg processor.
     *
     * @return the advice msg processor
     */
    public IMsgProcessor<ISOMsg> getAdviceMsgProcessor() {
        return adviceMsgProcessor;
    }

    /**
     * Sets advice msg processor.
     *
     * @param adviceMsgProcessor the advice msg processor
     */
    public void setAdviceMsgProcessor(IMsgProcessor<ISOMsg> adviceMsgProcessor) {
        this.adviceMsgProcessor = adviceMsgProcessor;
    }

    @Override
    public void process(ChannelHandlerContext channelHandlerContext, MsgContext<ISOMsg> msg) {
        IMsgProcessor<ISOMsg> msgHandler = requestMsgProcessor;
        if (isAdvice(msg.getMsg())) {
            if (adviceMsgProcessor != null)
                msgHandler = adviceMsgProcessor;
        }
        {
            if (msgHandler == null)
                throw new RuntimeException("can not found msg handler with MTI=" + msg.getMsg().getMTI());
            msgHandler.process(channelHandlerContext, msg);

        }

    }
}
