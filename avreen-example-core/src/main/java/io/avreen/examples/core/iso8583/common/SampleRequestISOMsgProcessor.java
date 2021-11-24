package io.avreen.examples.core.iso8583.common;


import io.avreen.common.context.MsgContext;
import io.avreen.common.netty.IMsgProcessor;
import io.avreen.iso8583.common.ISOMsg;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The class Sample request iso msg processor.
 */
public class SampleRequestISOMsgProcessor implements IMsgProcessor<ISOMsg> {

    /**
     * The Atomic integer.
     */
    AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public void process(ChannelHandlerContext channelHandlerContext, MsgContext<ISOMsg> context) {
        int i = atomicInteger.incrementAndGet();
        if (context.expired()) {
            System.out.println("expire message");
            return;
        }
        System.out.print("*");
        if (i % 100 == 0) {
            System.out.println();
            atomicInteger.set(0);
        }
        context.getMsg().dump(System.out, "");
        ISOMsg resp = MessageCreator.createResponse(context.getMsg());
        resp.dump(System.out, "");
        channelHandlerContext.channel().writeAndFlush(resp);
    }
}
