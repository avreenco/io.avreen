package io.avreen.common.util;


/**
 * The class Tps test case.
 */
public class TPSTestCase {

    private static TPS tps = new TPS();

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(tps);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        for (int idx = 0; idx < 100000000; idx++) {
            tps.tick();
            Thread.sleep(idx % 10);
        }
    }


}

