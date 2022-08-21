import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.currentThread;

public class SequenceConsolePrinter {
    private final List<Integer> preparedSequence;

    private final Semaphore semaphore1 = new Semaphore(1);
    private final Semaphore semaphore2 = new Semaphore(0);

    public SequenceConsolePrinter(List<Integer> preparedSequence) {
        this.preparedSequence = preparedSequence;
    }

    public void run() throws InterruptedException {
        Thread thread1 = new Thread(() -> serialPrint(semaphore1, semaphore2));
        thread1.setName("Thread-1");
        thread1.start();

        Thread thread2 = new Thread(() -> serialPrint(semaphore2, semaphore1));
        thread2.setName("Thread-2");
        thread2.start();

        thread1.join();
        thread2.join();
    }

    private void serialPrint(Semaphore semaphore1, Semaphore semaphore2) {
        ListIterator<Integer> iterator = preparedSequence.listIterator();
        while (iterator.hasNext() && !currentThread().isInterrupted()) {
            try {
                semaphore1.acquire();
                System.out.println(currentThread().getName() + ": " + iterator.next());
                semaphore2.release();
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        }
    }
}