package io.avreen.examples.core.iso8583.nettyclient;

import io.avreen.common.codec.tcp.ASCIIMessageLenCodec;
import io.avreen.common.netty.client.NettyASyncClient;
import io.avreen.examples.core.iso8583.common.MessageCreator;
import io.avreen.iso8583.channel.tcp.ISONettyClient;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.packager.impl.ISO87APackager;


/**
 * The class Client async example 0.
 */
public class client_async_example0 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        sendMessageSync();

    }

    private static ISONettyClient buildIsoNettyClient(int port) {
        ISONettyClient isoNettyClient =
                ISONettyClient.Builder()
                        .messageLenCodec(new ASCIIMessageLenCodec())
                        .isoPackager(new ISO87APackager())
                        .host("localhost")
                        .processor((channelHandlerContext, msg) -> msg.getMsg().dump(System.out, ""))
                        .port(port).build();
        return isoNettyClient;

    }

    /**
     * Send message sync.
     *
     * @throws Exception the exception
     */
    public static void sendMessageSync() throws Exception {
        ISONettyClient isoNettyClient1 = buildIsoNettyClient(6011);
        ISONettyClient isoNettyClient2 = buildIsoNettyClient(6012);
        NettyASyncClient<ISOMsg> nettySyncClient = new NettyASyncClient<>(isoNettyClient1, isoNettyClient2);
        nettySyncClient.start();
        while (!nettySyncClient.isConnected()) {
            System.out.println("channel not connected");
            Thread.sleep(100);
        }
        nettySyncClient.send(MessageCreator.createRequest());
        System.in.read();

    }

}
