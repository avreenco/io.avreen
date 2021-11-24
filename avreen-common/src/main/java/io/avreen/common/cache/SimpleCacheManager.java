package io.avreen.common.cache;

import io.avreen.common.context.CacheException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The class Simple cache manager.
 */
public class SimpleCacheManager implements ICacheManager {

    private static AtomicLong atomicLong = new AtomicLong();
    private TSpace isp = new TSpace();

    /**
     * Instantiates a new Simple cache manager.
     */
    public SimpleCacheManager() {
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     * @throws IOException          the io exception
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        SimpleCacheManager defaultCacheManager = new SimpleCacheManager();
        //defaultCacheManager.weakValue();
//        defaultCacheManager.putIfAbsent("1","2",30000);
//        Object h =  defaultCacheManager.remove("1");

        for (int idx = 0; idx < 100; idx++) {
            for (int i = 0; i < 1000; i++)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long start = System.currentTimeMillis();
                        String key = UUID.randomUUID().toString();
                        if (!defaultCacheManager.putIfAbsent(key, new BigDecimal(10), 3000))
                            System.err.println("************************************");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (defaultCacheManager.remove(key) == null)
                            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        long end = System.currentTimeMillis();
                        // System.out.println("time="+(end-start));
                    }
                }).start();
            Thread.yield();
        }
        System.in.read();
    }

    public boolean putIfAbsent(String req, Object msg, long timeout) throws CacheException {

        return isp.putIfAbsent(req, msg, timeout);
    }

    @Override
    public void put(String key, Object msg, long timeout) {
        isp.put(key, msg, timeout);
    }

    /**
     * Weak value.
     */
    public void weakValue() {
        isp.setWeakValue(true);
    }


    public Object remove(String key) {
        return isp.remove(key);
    }
    public void clear() {
         isp.clear();
    }

    @Override
    public Object get(String key) {
        return isp.get(key);
    }

    /**
     * Get t.
     *
     * @param <T>    the type parameter
     * @param key    the key
     * @param aClass the a class
     * @return the t
     */
    public <T> T get(String key, Class<T> aClass) {
        return (T) isp.get(key);
    }
}
