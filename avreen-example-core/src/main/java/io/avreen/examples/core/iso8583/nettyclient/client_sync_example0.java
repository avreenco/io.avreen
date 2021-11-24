package io.avreen.examples.core.iso8583.nettyclient;

import io.avreen.common.codec.tcp.ASCIIMessageLenCodec;
import io.avreen.common.netty.client.NettySyncClient;
import io.avreen.examples.core.iso8583.common.MessageCreator;
import io.avreen.iso8583.channel.tcp.ISONettyClient;
import io.avreen.iso8583.common.ISOMsg;
import io.avreen.iso8583.packager.impl.ISO87APackager;
import io.netty.channel.Channel;


/**
 * The class Client sync example 0.
 */
public class client_sync_example0 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        sendMessageSync();

    }

    /**
     * Send message sync.
     *
     * @throws Exception the exception
     */
    public static void sendMessageSync() throws Exception {
        ISONettyClient isoNettyClient =
                ISONettyClient.Builder()
                        .messageLenCodec(new ASCIIMessageLenCodec())
                        .isoPackager(new ISO87APackager())
                        .host("localhost")
                        .port(6011).build();
        NettySyncClient<ISOMsg> nettySyncClient = new NettySyncClient<>(isoNettyClient);
        Channel connectChannel = nettySyncClient.connect();
        ISOMsg resp = nettySyncClient.send(MessageCreator.createRequest(), 10000);
        nettySyncClient.disconnect(connectChannel);
        if (resp == null)
            System.out.println("timeout");
        else
            resp.dump(System.out, "");
        System.in.read();

    }

}
