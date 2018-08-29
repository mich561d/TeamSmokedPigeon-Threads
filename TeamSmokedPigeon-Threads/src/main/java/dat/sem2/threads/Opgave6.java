package dat.sem2.threads;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Opgave6 {

    public static void main(String[] args) throws Exception {
        /*
        Alle skal være den samme men bare have flere forskellige funktion
        så man bare tager den næste ledige tråd, og får den til at køre
        det man skal bruge.
        */
        ExecutorService workWork = Executors.newFixedThreadPool(3);
        // FileRunner
        Runnable file = new FileRunner();
        workWork.submit(file);
        // CalculatorRunner
        Runnable calc = new CalculatorRunner();
        workWork.submit(calc);
        // ExceptionsRunner
        Runnable exce = new ExceptionsRunner();
        workWork.submit(exce);
        // Server
        picoServer06();
        workWork.shutdown();

    }

    private static void picoServer06() throws Exception {
        final ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");
        String root = "pages";
        int count = 0;
        while (true) { // keep listening (as is normal for a server)
            Socket socket = server.accept();;
            try {
                System.out.println("---- reqno: " + count + " ----");
                HttpRequest req = new HttpRequest(socket.getInputStream());
                String path = req.getPath();
                if (path.endsWith(".html") || path.endsWith(".txt")) {
                    String html = getResourceFileContents(root + path);
                    String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + html;
                    socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                } else {
                    String res = "";
                    switch (path) {
                        case "/addournumbers":
                            res = addOurNumbers(req);
                            break;
                        default:
                            res = "Unknown path: " + path;
                    }
                    String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + res;
                    socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                }
            } catch (Exception ex) {
                String httpResponse = "HTTP/1.1 500 Internal error\r\n\r\n"
                        + "UUUUPS: " + ex.getLocalizedMessage();
                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
            count++;
        }
    }
}

class FileRunner implements Runnable {

    String root;
    String path;
    String html;
    String httpResponse;
    Socket socket;

    public void setRoot(String root) {
        this.root = root;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setHtml() throws Exception {
        this.html = getResourceFileContents(root + path);
    }

    public void setHttpResponse() {
        this.httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + html;
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
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource(fileName);
        File file = new File(url.getFile());
        String content = new String(Files.readAllBytes(file.toPath()));
        return content;

    }
}

class CalculatorRunner implements Runnable {

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static String addOurNumbers(HttpRequest req) {
        String first = req.getParameter("firstnumber");
        String second = req.getParameter("secondnumber");
        int fi = Integer.parseInt(first);
        int si = Integer.parseInt(second);
        String res = RES;
        res = res.replace("$0", first);
        res = res.replace("$1", second);
        res = res.replace("$2", String.valueOf(fi + si));
        return res;
    }
}

class ExceptionsRunner implements Runnable {

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
