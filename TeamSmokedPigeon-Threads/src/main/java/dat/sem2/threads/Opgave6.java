package dat.sem2.threads;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Opgave6 {

    public static void main(String[] args) {
        ExecutorService workWork = Executors.newFixedThreadPool(3);
        System.out.println("Main starts");
        for (int count = 0; count < 3; count++) {
            Runnable task = new Runner4(count);
            workWork.submit(task);
        }
        System.out.println("Main is done");
        workWork.shutdown();

    }

}

class FileRunner implements Runnable {

    @Override
    public void run() {

    }

    public String reader(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource(fileName);
        File file = new File(url.getFile());
        String content = new String(Files.readAllBytes(file.toPath()));
        return content;
    }
}
