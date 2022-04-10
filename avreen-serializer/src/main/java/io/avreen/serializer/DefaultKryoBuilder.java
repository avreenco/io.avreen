package io.avreen.serializer;


import io.netty.util.internal.SystemPropertyUtil;

/**
 * The class Default kryo builder.
 */
class DefaultKryoBuilder {

    private static int kryoPoolSize = SystemPropertyUtil.getInt("io.avreen.serializer.kryo.pool-size", 10000);

    /**
     * Build kry pool kryo pool.
     *
     * @return the kryo pool
     */
    public static KryoPool buildKryPool() {

        //builder.queue(new ArrayBlockingQueue<Kryo>(kryoPoolSize));
        //return builder.build();
        return new KryoPool(true, true, kryoPoolSize);
    }

}
