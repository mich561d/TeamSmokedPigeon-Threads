package dat.sem2.threads;

import java.util.ArrayList;

public class Opgave1 {

    public static void main(String[] args) throws InterruptedException {
        Database db = new Database();
        db.generateStrings();
        final int hmWorkers = 4;
        Thread[] threads = new Thread[hmWorkers];
        for (int index = 0; index < hmWorkers; index++) {
            threads[index] = new Thread(new Worker(db));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }
}

class Worker implements Runnable {

    private final Database db;

    public Worker(Database db) {
        this.db = db;
    }

    @Override
    public void run() {
        while (!db.isEmpty()) {
            String temp = db.getString();
            System.out.println(temp);
        }
    }

}

class Database {

    private ArrayList<String> strings = new ArrayList();

    public void generateStrings() {
        int max = (int) 'Z' - (int) 'A';
        for (int i = 0; i < max + 1; i++) {
            char c = (char) (i + 65);
            String temp = "" + c + c + c;
            strings.add(temp);
        }
    }

    public boolean isEmpty() {
        return strings.isEmpty();
    }

    public synchronized String getString() {
        String temp = strings.get(0);
        strings.remove(0);
        return temp;
    }
}
