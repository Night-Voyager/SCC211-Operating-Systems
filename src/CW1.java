import java.util.concurrent.Semaphore;

class ProcessSynchronization implements Runnable {
    private static int money = 0;

    private static final Semaphore lowerLimit = new Semaphore(0);
    private static final Semaphore upperLimit = new Semaphore(10);
    private static final Semaphore mutex = new Semaphore(1);

    @Override
    public void run() {
        while (true) {
            long threadID = Thread.currentThread().getId();

            if (threadID %2==1) {
                // When the thread ID is odd, increase money by 1.

                try {
                    upperLimit.acquire();
                    mutex.acquire();
                } catch (InterruptedException e) {
                    System.out.println();
                    e.printStackTrace();
                }

                money++;
                System.out.println(
                        "Thread Name: " + Thread.currentThread().getName()
                                + "\tThread ID: " + threadID
                                + "\tMoney: " + money
                );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mutex.release();
                lowerLimit.release();
            }
            else {
                // When the thread ID is even, reduce money by 1.

                try {
                    lowerLimit.acquire();
                    mutex.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                money--;
                System.out.println(
                        "Thread Name: " + Thread.currentThread().getName()
                                + "\tThread ID: " + threadID
                                + "\tMoney: " + money
                );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mutex.release();
                upperLimit.release();
            }
        }
    }
}

public class CW1 {
    public static void main(String[] args) {
        ProcessSynchronization processSynchronization = new ProcessSynchronization();

        for (int i = 0; i <= 10; i++) {
            Thread thread = new Thread(processSynchronization);
            thread.start();
            System.out.println("Starting the " + (i+1) + " thread.");
        }
    }
}
