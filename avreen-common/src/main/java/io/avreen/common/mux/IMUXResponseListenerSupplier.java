package io.avreen.common.mux;

import io.avreen.common.context.MsgContext;

/**
 * The interface Imux response listener supplier.
 *
 * @param <T> the type parameter
 */
public interface IMUXResponseListenerSupplier<T> {
    /**
     * Gets response listener.
     *
     * @param requestContext the request context
     * @return the response listener
     */
    IMUXResponseListener<T> getResponseListener(MsgContext<T> requestContext);
}
