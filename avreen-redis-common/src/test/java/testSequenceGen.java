import io.avreen.redis.common.RedisSequenceGenerator;
import io.lettuce.core.RedisClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Avreen Message Processor (Switch) Project create by hadi asgharnejad khoee
 * Copyright (C) 2017-2020 j
 * <p>
 * This program is not free software
 * The type Test sequence gen.
 */
public class testSequenceGen implements Runnable {
    /**
     * The All trace.
     */
    static ArrayList<Long> allTrace = new ArrayList<>();

    private static RedisSequenceGenerator redisSequenceGenerator;


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException          the io exception
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws IOException, InterruptedException {


        RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379");
        redisSequenceGenerator = new RedisSequenceGenerator(redisClient.connect().sync(), "trace",20);
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(new testSequenceGen());
            thread.start();
            thread.join();
        }
        // System.in.read();
        allTrace.sort(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1.compareTo(o2);
            }
        });

        long temp = allTrace.get(0);
        boolean notOK = false;
        for (int idx = 1; idx < allTrace.size(); idx++) {
            if ((allTrace.get(idx) - temp) != 1) {
                notOK = true;
                break;
            }
            temp = allTrace.get(idx);
        }
        System.out.println("size------------->" + allTrace.size() + "<<OK>>>" + !notOK);

    }

    @Override
    public void run() {
        for (int idx = 0; idx < 1000; idx++) {
            long trace = 0;
            try {
                trace = redisSequenceGenerator.generate();
                synchronized (allTrace) {
                    allTrace.add(trace);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
