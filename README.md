# io.avreen
message base framework iso8583 , ...
this librarry is simple messaging base on netty and support iso8583 message format and http
iso8583 packager base ( only pack and unpack not channel and container ) copy from jpos open source project
include
  channel abstraction ( send & receive )
  message context
  message queue abstraction for sclae process
  ....

# ISO Server Example
    import io.avreen.common.codec.tcp.ASCIIMessageLenCodec;
    import io.avreen.examples.core.iso8583.common.SampleRequestISOMsgProcessor;
    import io.avreen.iso8583.channel.tcp.ISONettyServerBuilder;
    import io.avreen.iso8583.packager.impl.ISO87APackager;
    import java.time.Duration;
    
    new ISONettyServerBuilder()
            .messageLenCodec(new ASCIIMessageLenCodec())
            .isoPackager(new ISO87APackager())
            .idleTime(Duration.ofSeconds(10))
            .port(6012).processor((channelHandlerContext, msg) -> {
                ISOMsg resp_msg = msg.getMsg();
                resp_msg.setResponseMTI();
                channelHandlerContext.writeAndFlush(resp_msg);
            }).build().start();


# ISO Sync Client Example 
    import io.avreen.common.codec.tcp.ASCIIMessageLenCodec;
    import io.avreen.common.netty.client.NettySyncClient;
    import io.avreen.examples.core.iso8583.common.MessageCreator;
    import io.avreen.iso8583.channel.tcp.ISONettyClient;
    import io.avreen.iso8583.channel.tcp.ISONettyClientBuilder;
    import io.avreen.iso8583.common.ISOMsg;
    import io.avreen.iso8583.packager.impl.ISO87APackager;
    import io.netty.channel.Channel;
    
     ISOMsg isoMsg  =new ISOMsg()
    .setMTI("0200")
    .set(3, "010000")
    .set(11, 126589)
    .set(41, "t111111")
    .set(35, 621986101234567890=876554644543444)
    .set(42, "HADI")
    .set(52, ISOUtil.hex2byte("1234567890123456"))
    .set(63, "1234567890");
    
    ISONettyClient isoNettyClient =
            new ISONettyClientBuilder()
                    .messageLenCodec(new ASCIIMessageLenCodec())
                    .isoPackager(new ISO87APackager())
                    .host("localhost")
                    .port(6011).build();
    NettySyncClient<ISOMsg> nettySyncClient = new NettySyncClient<>(isoNettyClient);
    Channel connectChannel = nettySyncClient.connect();
    ISOMsg resp = nettySyncClient.send(isoMsg, 10000);
    nettySyncClient.disconnect(connectChannel);
    if (resp == null)
        System.out.println("timeout");
    else
        resp.dump(System.out, "");

# ISO ASync Client Example

    ISONettyClient isoNettyClient1 = buildIsoNettyClient(6011);
    ISONettyClient isoNettyClient2 = buildIsoNettyClient(6012);
    NettyASyncClient<ISOMsg> nettySyncClient = new NettyASyncClient<>(isoNettyClient1, isoNettyClient2);
    nettySyncClient.start();
    while (!nettySyncClient.isConnected()) {
        System.out.println("channel not connected");
        Thread.sleep(100);
    }
    nettySyncClient.send(isoMsg);
    System.in.read();
