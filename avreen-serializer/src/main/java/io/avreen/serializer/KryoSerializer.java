package io.avreen.serializer;

import io.avreen.serializer.api.ISerializer;
import io.avreen.serializer.api.ISerializerListener;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * The class Kryo serializer.
 */
class KryoSerializer implements ISerializer {
    private KryoPool kryoPool;
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".serializer.KryoSerializer");

    /**
     * Instantiates a new Kryo serializer.
     *
     * @param kryoPool the kryo pool
     */
    public KryoSerializer(KryoPool kryoPool) {
        this.kryoPool = kryoPool;
    }

    public <T> T readObject(byte[] bytes, Class<T> types) {
        T t = KryoUtil.readObject(kryoPool, bytes, types);
        if (t instanceof ISerializerListener)
            ((ISerializerListener) t).afterDeserialize(false);
        return t;
    }

    public byte[] writeObject(Object msg) {
        return KryoUtil.writeObject(kryoPool, msg);
    }

    public byte[] writeClassAndObject(Object msg) {
        return KryoUtil.writeClassAndObject(kryoPool, msg);
    }

    public Object readClassAndObject(byte[] bytes) {
        Object t = KryoUtil.readClassAndObject(kryoPool, bytes);
        if (t instanceof ISerializerListener)
            ((ISerializerListener) t).afterDeserialize(false);
        return t;
    }
}
