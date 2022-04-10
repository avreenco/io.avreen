package io.avreen.serializer.api;

/**
 * The interface Serializer.
 */
public interface ISerializer {
    /**
     * Read object t.
     *
     * @param <T>   the type parameter
     * @param bytes the bytes
     * @param types the types
     * @return the t
     */
    <T> T readObject(byte[] bytes, Class<T> types);

    /**
     * Write object byte [ ].
     *
     * @param msg the msg
     * @return the byte [ ]
     */
    byte[] writeObject(Object msg);

    /**
     * Write class and object byte [ ].
     *
     * @param msg the msg
     * @return the byte [ ]
     */
    byte[] writeClassAndObject(Object msg);

    /**
     * Read class and object object.
     *
     * @param bytes the bytes
     * @return the object
     */
    Object readClassAndObject(byte[] bytes);

}
