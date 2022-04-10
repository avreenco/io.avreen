import io.avreen.serializer.KryoPool;
import io.avreen.serializer.KryoUtil;

import java.net.InetSocketAddress;

/**
 * The class Test kryo.
 */
public class testKryo2 {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        KryoPool kryoPool = new KryoPool(true, true, 10000000);
        byte[] bytes = KryoUtil.writeObject(kryoPool, new InetSocketAddress(10));
        InetSocketAddress inetSocketAddress = KryoUtil.readObject(kryoPool, bytes, InetSocketAddress.class);
        System.out.println(inetSocketAddress);


    }

}
