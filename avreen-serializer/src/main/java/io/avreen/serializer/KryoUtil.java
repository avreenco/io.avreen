package io.avreen.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.Output;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.ByteArrayOutputStream;

/**
 * The class Kryo util.
 */
public class KryoUtil {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(LoggerDomain.Name + ".serializer.KryoUtil");

    /**
     * Read object t.
     *
     * @param <T>      the type parameter
     * @param kryoPool the kryo pool
     * @param bytes    the bytes
     * @param types    the types
     * @return the t
     */
    public static <T> T readObject(KryoPool kryoPool, byte[] bytes, Class<T> types) {
        Kryo kryo = kryoPool.obtain();
        //ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        //Input in = new Input(bis);
        ByteBufferInput in = new ByteBufferInput(bytes);
        try {
            return kryo.readObject(in, types);
        } finally {
            kryoPool.free(kryo);
            in.close();
        }
    }

    /**
     * Read class and object object.
     *
     * @param kryoPool the kryo pool
     * @param bytes    the bytes
     * @return the object
     */
    public static Object readClassAndObject(KryoPool kryoPool, byte[] bytes) {
        Kryo kryo = kryoPool.obtain();
        //ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        //Input in = new Input(bis);
        ByteBufferInput in = new ByteBufferInput(bytes);
        try {
            return kryo.readClassAndObject(in);
        } finally {
            kryoPool.free(kryo);
            in.close();
        }
    }

    /**
     * Write object byte [ ].
     *
     * @param kryoPool the kryo pool
     * @param msg      the msg
     * @return the byte [ ]
     */
    public static byte[] writeObject(KryoPool kryoPool, Object msg) {
        Kryo kryo = kryoPool.obtain();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output out = new Output(bos);
        try {
            kryo.writeObject(out, msg);
            out.flush();
            byte[] bytes = bos.toByteArray();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("write object. type={}  byte length={}", msg.getClass(), bytes.length);
            return bytes;
        } catch (Exception e) {
            LOGGER.error("writeObject error object={}", msg, e);
            throw new RuntimeException(e);
        } finally {
            kryoPool.free(kryo);
            out.close();
        }
    }

    /**
     * Write class and object byte [ ].
     *
     * @param kryoPool the kryo pool
     * @param msg      the msg
     * @return the byte [ ]
     */
    public static byte[] writeClassAndObject(KryoPool kryoPool, Object msg) {
        Kryo kryo = kryoPool.obtain();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output out = new Output(bos);
        try {
            kryo.writeClassAndObject(out, msg);
            out.flush();
            byte[] bytes = bos.toByteArray();
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("write object and class. type={}  byte length={}", msg.getClass(), bytes.length);

            return bytes;
        } catch (Exception e) {
            LOGGER.error("writeObject error object={}", msg, e);
            throw new RuntimeException(e);
        } finally {
            kryoPool.free(kryo);
            out.close();
        }
    }


}
