package dat.sem2.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Opgave4 {

    public static void main(String[] args) {
        ExecutorService workingJack = Executors.newFixedThreadPool(10000);
        //System.out.println("Main starts");
        for (int count = 0; count < 100; count++) {
            Runnable task = new RunnerY(count);
            workingJack.submit(task);
        }
        //System.out.println("Main is done");
        workingJack.shutdown();
        
    }

}

class RunnerY implements Runnable {

    private int count = 0;
    private int sleepTime = 0;

    private static int hundred = 100;

    RunnerY(int cnt) {
        sleepTime = (int) (Math.random() * 800 + 200); // At least 200 ms, up to one sec
        count = cnt;
    }

    @Override
    public void run() {
        synchronized (Runner4.class) { // skal synchronized være inde i trycatch eller omvendt?
            try {
                int myH = hundred;
                Thread.sleep(sleepTime); // simulate some external job taking time
                myH += 100;
                hundred = myH;
                System.out.println("Task: " + count + ": Hundred = " + hundred);
            } catch (InterruptedException ex) {
                System.out.println("We got interrupted");
            }
        }
    }
}
