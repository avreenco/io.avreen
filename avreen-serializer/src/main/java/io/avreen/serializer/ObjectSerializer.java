package io.avreen.serializer;

import io.avreen.serializer.api.ISerializer;

/**
 * The class Object serializer.
 */
public class ObjectSerializer {
    private static KryoPool kryoPool;
    private static KryoSerializer kryoSerializer;

    static {
        kryoPool = DefaultKryoBuilder.buildKryPool();
        kryoSerializer = new KryoSerializer(kryoPool);
    }

    /**
     * Current serializer.
     *
     * @return the serializer
     */
    public static ISerializer current() {
        return kryoSerializer;
    }


}
