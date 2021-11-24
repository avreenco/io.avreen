package io.avreen.common.netty;

import io.avreen.common.context.MessageTypes;

/**
 * The interface Message type supplier.
 */
public interface IMessageTypeSupplier
{
    /**
     * Gets message types.
     *
     * @return the message types
     */
    MessageTypes getMessageTypes();

    default void setResponse(){}
}
