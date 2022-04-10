package io.avreen.http.channel;

import io.avreen.common.context.MsgContext;
import io.avreen.common.netty.client.ConnectionModel;
import io.avreen.common.netty.client.NettyASyncClient;
import io.avreen.common.netty.client.NettyClientBase;
import io.avreen.http.common.HttpMsg;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The type Http async client.
 */
public class HttpClientTemplate extends NettyASyncClient<HttpMsg> {
    /**
     * Instantiates a new Http async client.
     *
     * @param nettyClients the netty clients
     */
    public HttpClientTemplate(NettyClientBase<HttpMsg>... nettyClients) {
        super(nettyClients);
    }


    @Override
    public synchronized void start() {
        setConnectionModel(ConnectionModel.OnDemand);
        setResponseListener(this::onReceive);
        super.start();
    }

    private void onReceive(MsgContext<HttpMsg> request, HttpMsg response, Object handBack) {
        CompletableFuture<HttpMsg> completableFuture = (CompletableFuture<HttpMsg>) handBack;
        completableFuture.complete(response);
    }

    public HttpMsg sendSync(MsgContext<HttpMsg> msgContext, long timeout) throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<HttpMsg> completableFuture = new CompletableFuture<>();
        send(msgContext, completableFuture);
        return completableFuture.get(timeout, TimeUnit.MILLISECONDS);
    }

}
