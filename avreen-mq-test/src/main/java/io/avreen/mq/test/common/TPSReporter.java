package io.avreen.mq.test.common;

import io.avreen.common.util.TPS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The class Test.
 */
public class TPSReporter {
    private TPS tps;

    public TPSReporter(TPS tps) {
        this.tps = tps;
    }

    public void start() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        long delay  = 0L;
        long period = 1000L;
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(tps);
            }
        }, delay, period, TimeUnit.MILLISECONDS);

    }

}
