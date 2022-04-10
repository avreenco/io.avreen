package testFunc;

import io.avreen.common.mux.IMultiplexer;
import org.avreen.security.module.api.RequestBase;
import org.avreen.security.module.api.ResponseBase;
import org.avreen.security.module.api.SecurityFunctionMessage;
import org.avreen.security.module.impl.hsm.safenet.impl.SafeNetMessageSender;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Sender.
 */
public class sender {

    /**
     * Create multiplexer multiplexer.
     *
     * @return the multiplexer
     */
    public static IMultiplexer<SecurityFunctionMessage> createMultiplexer() {
//        SafeNetNettyClient safeNetNettyClient = new SafeNetNettyClient("192.168.0.173", 9002);
//        safeNetNettyClient.setName("safenet1");
//        safeNetNettyClient.start();
//        NettyASyncClient<SecurityFunctionMessage> nettySyncClient = new NettyASyncClient<>(safeNetNettyClient);
//        nettySyncClient.start();
//        DefaultMultiplexer<SecurityFunctionMessage> defaultMultiplexer = new DefaultMultiplexer<>();
//        defaultMultiplexer.setKeyProvider(SafeNetMUXKeyProvider.INSTANCE);


//        ASyncClientMultiplexer<SecurityFunctionMessage> multiplexer = new ASyncClientMultiplexer<>(nettySyncClient, defaultMultiplexer);
//        while (!nettySyncClient.isConnected()) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("not conected hsm");
//
//        }
//        return multiplexer;
        return null;
    }

    /**
     * Send t.
     *
     * @param <T> the type parameter
     * @param g   the g
     * @return the t
     * @throws Exception the exception
     */
    public static <T extends ResponseBase> T send(RequestBase g) throws Exception {

        IMultiplexer<SecurityFunctionMessage> multiplexer = createMultiplexer();
        return sender.send(multiplexer, g);

    }

    /**
     * Send t.
     *
     * @param <T>         the type parameter
     * @param multiplexer the multiplexer
     * @param g           the g
     * @return the t
     */
    public static <T extends ResponseBase> T send(IMultiplexer<SecurityFunctionMessage> multiplexer, RequestBase g) {

        if (g.getHeader() == null)
            g.buildHeader();
        System.out.println(">>>>>>>>>>>>>>>>>>sending msg=");
        System.out.println(g);
        T r = SafeNetMessageSender.send(multiplexer, g, 10000);
        if (r.isApprove()) {
            System.out.println("<<<<<<<<<<<<<<<<<<<recived approve msg=");
            System.out.println(r);
        } else {
            System.out.println("<!!!!!!!!!!!!!!!!!!!!!! recived not approve msg=");
            System.out.println(r);
        }

        return r;
    }
}
