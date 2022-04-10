package io.avreen.examples.core.iso8583.nettyserver;

import io.avreen.common.codec.tcp.ASCIIMessageLenCodec;
import io.avreen.examples.core.iso8583.common.SampleRequestISOMsgProcessor;
import io.avreen.iso8583.channel.SLF4JISOMsgLogHandler;
import io.avreen.iso8583.channel.tcp.ISONettyServerBuilder;
import io.avreen.iso8583.mapper.impl.ISO87AMapper;
import io.netty.channel.ChannelHandler;

import java.time.Duration;
import java.util.function.Supplier;


/**
 * The class Server example 0.
 */
public class server_example0 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new ISONettyServerBuilder()
                .messageLenCodec(new ASCIIMessageLenCodec())
                .isoMapper(new ISO87AMapper())
                .idleTime(Duration.ofSeconds(10))
                .addAdditionalChannelHandler(() -> new SLF4JISOMsgLogHandler())
                .port(6011).processor(new SampleRequestISOMsgProcessor()).build().start();
        new ISONettyServerBuilder()
                .messageLenCodec(new ASCIIMessageLenCodec())
                .isoMapper(new ISO87AMapper())
                .idleTime(Duration.ofSeconds(10))
                .port(6012).processor(new SampleRequestISOMsgProcessor()).build().start();




    }

}
