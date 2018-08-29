package dat.sem2.threads;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Opgave6 {

    public static void main(String[] args) {
        ExecutorService workWork = Executors.newFixedThreadPool(3);
        System.out.println("Main starts");

        System.out.println("Main is done");
        workWork.shutdown();

    }

}

class FileRunner implements Runnable {

    String root;
    String path;
    String html;
    String httpResponse;
    Socket socket;

    public FileRunner(String root, String path, Socket socket) throws Exception {
        this.root = root;
        this.path = path;
        this.socket = socket;
        html = getResourceFileContents(root + path);
        httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + html;
    }

    @Override
    public void run() {
        try {
            socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        } catch (IOException ex) {
            Logger.getLogger(FileRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String getResourceFileContents(String fileName) throws Exception {
        //Get file from resources folder
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource(fileName);
        File file = new File(url.getFile());
        String content = new String(Files.readAllBytes(file.toPath()));
        return content;

    }
}
