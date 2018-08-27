package dat.sem2.threads;

/**
 * The purpose of ThreadsSync01 is to...
 *
 * @author kasper
 */
public class ThreadsSyncSafe {

    public static void main(String[] args) throws InterruptedException {
        final int noThreads = 1000;
        final int noLoops = 1000;
        final int addNumber = 15;
        final int expectedSum = noThreads * noLoops * addNumber;
        final CounterSafe c = new CounterSafe(0);
        Thread[] threads = new Thread[noThreads];
        for (int index = 0; index < noThreads; index++) {
            threads[index] = new Thread(new XRunner(noLoops, addNumber, c));
        }
        for (Thread t : threads) {
            t.start();
        }
        System.out.println("All started");
        for (Thread t : threads) {
            t.join();
        }
        System.out.println("\nSum (expected " + expectedSum + ") is: " + c.getCounter());
    }
}

class XRunner implements Runnable {
    
    private final int noLoops;
    private final int addNumber;
    private final CounterSafe c;

    public XRunner(int noLoops, int addNumber, CounterSafe c) {
        this.noLoops = noLoops;
        this.addNumber = addNumber;
        this.c = c;
    }

    @Override
    public void run() {
        for (int i = 0; i < noLoops; i++) {
            c.add(addNumber);
        }
    }

}

class CounterSafe {

    private int counter;

    public CounterSafe(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public synchronized void add(int amount) {
        int old = counter;
        counter = old + amount;
    }
}
