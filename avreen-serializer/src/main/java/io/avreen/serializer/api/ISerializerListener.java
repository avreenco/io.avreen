package io.avreen.serializer.api;

/**
 * The interface Serializer listener.
 */
public interface ISerializerListener {

    /**
     * After deserialize.
     *
     * @param containClassInfo the contain class info
     */
    void afterDeserialize(boolean containClassInfo);

}
