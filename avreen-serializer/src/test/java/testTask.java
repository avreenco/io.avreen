import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * The class Test task.
 */
public class testTask {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        FutureTask<String> futureTask1 = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("start task");
                Thread.sleep(3000);
                System.out.println("end task task");
                return Thread.currentThread().getName();
            }
        });
        FutureTask<String> futureTask2 = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("start task");
                Thread.sleep(3000);
                System.out.println("end task task");
                return Thread.currentThread().getName();
            }
        });
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(futureTask1);
        executor.submit(futureTask2);
        //boolean cancel = futureTask1.cancel(true);
    }
}
