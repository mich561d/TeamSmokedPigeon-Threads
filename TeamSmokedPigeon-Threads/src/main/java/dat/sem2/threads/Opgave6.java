package dat.sem2.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Opgave6 {

    public static void main(String[] args) {
        ExecutorService workWork = Executors.newFixedThreadPool(3);
        System.out.println("Main starts");

        System.out.println("Main is done");
        workWork.shutdown();

    }

}
