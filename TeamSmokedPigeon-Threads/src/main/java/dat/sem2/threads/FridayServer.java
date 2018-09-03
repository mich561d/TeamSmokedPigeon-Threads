package dat.sem2.threads;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FridayServer {

    public static void main( String[] args ) throws Exception {
        picoServer06Executor();
    }

    /*
    This server has exception handling - so if something goes wrong we do not
    have to start it again. (this is a yellow/red thing for now)
     */
    private static void picoServer05Executor() throws Exception {
        final ServerSocket server = new ServerSocket( 8080 );
        System.out.println( "Listening for connection on port 8080 ...." );
        String root = "pages";
        ExecutorService workers = Executors.newFixedThreadPool( 3 );
        while ( true ) { // keep listening (as is normal for a server)
            Socket socket = server.accept();
            workers.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        MyFileReader( socket, root );
                    } catch ( IOException ex ) {
                        System.out.println( "We got an error: " + ex.getMessage() );
                    }
                }
            });
        }
    }

    private static void MyFileReader( Socket socket, String root ) throws IOException {
        try {
            System.out.println( "-----------------" );
            HttpRequest req = new HttpRequest( socket.getInputStream() );
            String path = root + req.getPath();
            String html = getResourceFileContents( path );
            String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + html;
            socket.getOutputStream().write( httpResponse.getBytes( "UTF-8" ) );
            System.out.println( "<<<<<<<<<<<<<<<<<" );
        } catch ( Exception ex ) {
            String httpResponse = "HTTP/1.1 500 Internal error\r\n\r\n"
                    + "UUUUPS: " + ex.getLocalizedMessage();
            socket.getOutputStream().write( httpResponse.getBytes( "UTF-8" ) );
        } finally {
            if ( socket != null ) {
                socket.close();
            }
        }
    }

    /*
    This server requires static files to be named ".html" or ".txt". Other path
    names is assumed to be a name of a service.
     */
    private static void picoServer06Executor() throws Exception {
        final ServerSocket server = new ServerSocket( 8080 );
        final ExecutorService workingJack = Executors.newFixedThreadPool( 4 );
        System.out.println( "Listening for connection on port 8080 ...." );
        String root = "pages";
        int count = 0;
        while ( true ) { // keep listening (as is normal for a server)
            Socket socket = server.accept();
            System.out.println( "---- reqno: " + count + " ----" );
                workingJack.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        handleRequestTask( socket, workingJack, root );
                    } catch ( IOException ex ) {
                        System.out.println( "We got an error: " + ex.getMessage() );
                    }
                }
            });
            count++;
        }
    }

    private static void handleRequestTask( Socket socket, ExecutorService workingJack, String root ) throws IOException {
        HttpRequest req = new HttpRequest( socket.getInputStream() );
        String path = req.getPath();
        if ( path.endsWith( ".html" ) || path.endsWith( ".txt" ) ) {
            workingJack.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        getFileTask( root, path, socket );
                    } catch ( Exception ex ) {
                        System.out.println( "We got an error: " + ex.getMessage() );
                    }
                }
            });
        } else {
            workingJack.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        addourNumbersTask( path, req, socket );
                    } catch ( IOException ex ) {
                        System.out.println( "We got an error: " + ex.getMessage() );
                    }
                }
            });
        }
    }

    private static void addourNumbersTask( String path, HttpRequest req, Socket socket ) throws IOException {
        String res = "";
        switch ( path ) {
            case "/addournumbers":
                res = addOurNumbers( req );
                break;
            default:
                res = "Unknown path: " + path;
        }
        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + res;
        socket.getOutputStream().write( httpResponse.getBytes( "UTF-8" ) );
        socket.close();
    }

    private static void getFileTask( String root, String path, Socket socket ) throws IOException, Exception {
        String html = getResourceFileContents( root + path );
        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + html;
        socket.getOutputStream().write( httpResponse.getBytes( "UTF-8" ) );
        socket.close();
    }

    /*
    It is not part of the curriculum (pensum) to understand this method.
    You are more than welcome to bang your head on it though.
     */
    private static String getResourceFileContents( String fileName ) throws Exception {
        //Get file from resources folder
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource( fileName );
        File file = new File( url.getFile() );
        String content = new String( Files.readAllBytes( file.toPath() ) );
        return content;

    }

    private static String addOurNumbers( HttpRequest req ) {
        String first = req.getParameter( "firstnumber" );
        String second = req.getParameter( "secondnumber" );
        int fi = Integer.parseInt( first );
        int si = Integer.parseInt( second );
        String res = RES;
        res = res.replace( "$0", first );
        res = res.replace( "$1", second );
        res = res.replace( "$2", String.valueOf( fi + si ) );
        return res;
    }

    private static String RES = "<!DOCTYPE html>\n"
            + "<html lang=\"da\">\n"
            + "    <head>\n"
            + "        <title>Adding form</title>\n"
            + "        <meta charset=\"UTF-8\">\n"
            + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
            + "    </head>\n"
            + "    <body>\n"
            + "        <h1>Super: Resultatet af $0 + $1 blev: $2</h1>\n"
            + "        <a href=\"adding.html\">Læg to andre tal sammen</a>\n"
            + "    </body>\n"
            + "</html>\n";

}


