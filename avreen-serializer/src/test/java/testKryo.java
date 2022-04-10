import io.avreen.serializer.KryoPool;
import io.avreen.serializer.KryoUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class Test kryo.
 */
public class testKryo {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        KryoPool kryoPool = new KryoPool(true, true, 10000000);
        EKryoTest eKryoTest = new EKryoTest(10);
        eKryoTest.setX2(20);
        Map map = new ConcurrentHashMap();
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        eKryoTest.map = map;

        for (int idx = 0; idx < 1; idx++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] bytes = KryoUtil.writeObject(kryoPool, eKryoTest);
                    EKryoTest eKryoTest1 = KryoUtil.readObject(kryoPool, bytes, EKryoTest.class);
                    System.out.println("length=" + bytes.length);
                }
            }).start();
        }
        Integer[] k = new Integer[4];
        k[0] = 1;
        k[1] = 2;
        k[2] = 3;
        k[3] = 4;
        System.out.println("---------------" + Arrays.toString(k));


//        EKryoTest eKryoTest1 = KryoUtil.readObject(kryoPool, bytes, EKryoTest.class);
//        System.out.println(eKryoTest1.getX1() + "   " + eKryoTest1.getX2());


    }

}
