package io.avreen.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.util.Pool;

import java.net.InetSocketAddress;

/**
 * The class Kryo pool.
 */
public class KryoPool extends Pool<Kryo> {
    /**
     * Instantiates a new Kryo pool.
     *
     * @param threadSafe     the thread safe
     * @param softReferences the soft references
     */
    public KryoPool(boolean threadSafe, boolean softReferences) {
        super(threadSafe, softReferences);
    }

    /**
     * Instantiates a new Kryo pool.
     *
     * @param threadSafe      the thread safe
     * @param softReferences  the soft references
     * @param maximumCapacity the maximum capacity
     */
    public KryoPool(boolean threadSafe, boolean softReferences, int maximumCapacity) {
        super(threadSafe, softReferences, maximumCapacity);
    }

    @Override
    protected Kryo create() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(InetSocketAddress.class, new JavaSerializer());
        return kryo;
    }
}
