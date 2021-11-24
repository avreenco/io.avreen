
package io.avreen.common.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The class Tps.
 */
public class TPS {
    /**
     * The Count.
     */
    AtomicInteger count;
    /**
     * The Start.
     */
    AtomicLong start;
    /**
     * The Readings.
     */
    AtomicLong readings;
    /**
     * The Peak.
     */
    int peak;
    /**
     * The Peak when.
     */
    long peakWhen;
    /**
     * The From nanos.
     */
    static final long FROM_NANOS = 1000000L;
    /**
     * The Period.
     */
    long period;
    /**
     * The Tps.
     */
    float tps;
    /**
     * The Avg.
     */
    float avg;
    /**
     * The Timer.
     */
    Timer timer;
    /**
     * The Autoupdate.
     */
    boolean autoupdate;
    /**
     * The Lock.
     */
    final ReentrantLock lock = new ReentrantLock();
    /**
     * The Simulated nano time.
     */
    protected long simulatedNanoTime = 0L;

    /**
     * Instantiates a new Tps.
     */
    public TPS() {
        this(1000L, false);
    }

    /**
     * Instantiates a new Tps.
     *
     * @param autoupdate the autoupdate
     */
    public TPS(boolean autoupdate) {
        this(1000L, autoupdate);
    }

    /**
     * Instantiates a new Tps.
     *
     * @param period     the period
     * @param autoupdate the autoupdate
     */
    public TPS(final long period, boolean autoupdate) {
        super();
        count = new AtomicInteger(0);
        start = new AtomicLong(0L);
        readings = new AtomicLong(0L);
        this.period = period;
        this.autoupdate = autoupdate;
        start.set(System.nanoTime() / FROM_NANOS);
        if (autoupdate) {
            timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        public void run() {
                            calcTPS(period);
                        }
                    }, period, period);
        }
    }

    /**
     * Tick.
     */
    public void tick() {
        count.incrementAndGet();
    }

    /**
     * Float value float.
     *
     * @return the float
     */
    public float floatValue() {
        return autoupdate ? tps : calcTPS();
    }

    /**
     * Int value int.
     *
     * @return the int
     */
    public int intValue() {
        return Math.round(floatValue());
    }

    /**
     * Gets avg.
     *
     * @return the avg
     */
    public float getAvg() {
        return avg;
    }

    /**
     * Gets peak.
     *
     * @return the peak
     */
    public int getPeak() {
        return peak;
    }

    /**
     * Gets peak when.
     *
     * @return the peak when
     */
    public long getPeakWhen() {
        return peakWhen;
    }

    /**
     * Reset.
     */
    public void reset() {
        lock.lock();
        try {
            avg = 0f;
            peak = 0;
            peakWhen = 0L;
            readings.set(0L);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets period.
     *
     * @return the period
     */
    public long getPeriod() {
        return period;
    }

    /**
     * Gets elapsed.
     *
     * @return the elapsed
     */
    public long getElapsed() {
        return (System.nanoTime() / FROM_NANOS) - start.get();
    }

    public String toString() {
        return String.format("tps=%d, peak=%d, avg=%.2f", intValue(), getPeak(), getAvg());

    }

    /**
     * Stop.
     */
    public void stop() {
        lock.lock();
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
                autoupdate = false; // can still use it in manual mode
            }
        } finally {
            lock.unlock();
        }
    }

    private float calcTPS(long interval) {
        lock.lock();
        try {
            tps = (float) period * count.get() / interval;
            if (period != 1000L) {
                tps = tps * 1000L / period;
            }
            long r = readings.getAndIncrement();
            avg = (r * avg + tps) / ++r;
            if (tps > peak) {
                peak = Math.round(tps);
                peakWhen = System.currentTimeMillis();
            }
            count.set(0);
            return tps;
        } finally {
            lock.unlock();
        }
    }

    private float calcTPS() {
        lock.lock();
        try {
            long now = getNanoTime() / FROM_NANOS;
            long interval = now - start.get();
            if (interval >= period) {
                calcTPS(interval);
                start.set(now);
            }
            return tps;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets simulated nano time.
     *
     * @param simulatedNanoTime the simulated nano time
     */
    public void setSimulatedNanoTime(long simulatedNanoTime) {
        if (this.simulatedNanoTime == 0L)
            start.set(simulatedNanoTime / FROM_NANOS);

        this.simulatedNanoTime = simulatedNanoTime;
    }

    /**
     * Gets nano time.
     *
     * @return the nano time
     */
    protected long getNanoTime() {
        return simulatedNanoTime > 0L ? simulatedNanoTime : System.nanoTime();
    }
}
