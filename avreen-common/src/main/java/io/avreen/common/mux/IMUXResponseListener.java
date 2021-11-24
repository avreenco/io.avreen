package io.avreen.common.mux;

import io.avreen.common.context.MsgContext;

/**
 * The interface Imux response listener.
 *
 * @param <T> the type parameter
 */
public interface IMUXResponseListener<T> {
    /**
     * On receive.
     *
     * @param request  the request
     * @param response the response
     * @param handBack the hand back
     */
    void onReceive(MsgContext<T> request, T response, Object handBack);
}
