import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * The class Test task 2.
 */
public class testTask2 {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                CompletableFuture<String> stringCompletableFuture = new CompletableFuture<>();
                System.out.println(Thread.currentThread().getName() + "<<<<<run>>>>>>>>>>");
                stringCompletableFuture.whenCompleteAsync(new BiConsumer<String, Throwable>() {
                    @Override
                    public void accept(String s, Throwable throwable) {
                        System.out.println(Thread.currentThread().getName() + "------------->>>" + s);
                    }
                });
                stringCompletableFuture.whenCompleteAsync(new BiConsumer<String, Throwable>() {
                    @Override
                    public void accept(String s, Throwable throwable) {
                        System.out.println(Thread.currentThread().getName() + "==================>>>" + s);
                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(Thread.currentThread().getName() + "begin complete");
                            Thread.sleep(3000);
                            stringCompletableFuture.complete("Hadi");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }).start();


        System.in.read();

    }
}
