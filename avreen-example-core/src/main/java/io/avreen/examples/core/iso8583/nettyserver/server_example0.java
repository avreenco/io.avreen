package io.avreen.examples.core.iso8583.nettyserver;

import io.avreen.common.codec.tcp.ASCIIMessageLenCodec;
import io.avreen.examples.core.iso8583.common.SampleRequestISOMsgProcessor;
import io.avreen.iso8583.channel.tcp.ISONettyServer;
import io.avreen.iso8583.packager.impl.ISO87APackager;

import java.time.Duration;


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
        ISONettyServer.Builder()
                .messageLenCodec(new ASCIIMessageLenCodec())
                .isoPackager(new ISO87APackager())
                .idleTime(Duration.ofSeconds(10))
                .port(6011).processor(new SampleRequestISOMsgProcessor()).build().start();
        ISONettyServer.Builder()
                .messageLenCodec(new ASCIIMessageLenCodec())
                .isoPackager(new ISO87APackager())
                .idleTime(Duration.ofSeconds(10))
                .port(6012).processor(new SampleRequestISOMsgProcessor()).build().start();
    }

}
