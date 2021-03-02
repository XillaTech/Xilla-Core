package net.xilla.core.library.web;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

    @Getter
    private File webRoot;

    @Getter
    @Setter
    private String defaultFile = "index.html";

    @Getter
    @Setter
    private String fileNotFound = "404.html";

    @Getter
    @Setter
    private String methodNotSupported = "not_supported.html";

    @Getter
    @Setter
    private boolean isRunning = true;

    @Getter
    @Setter
    private boolean verbose = false;

    private ServerSocket serverSocket;

    private ExecutorService executor;

    private int port;

    public static final Hashtable<String, String> MIME_TYPES = new Hashtable<>();

    protected WebServer(ServerSocket socket, String directory, int port, int threads) throws IOException {
        serverSocket = new ServerSocket(port);

        if(directory == null || directory.isEmpty()) {
            directory = System.getProperty("user.dir");
        }

        this.webRoot = new File(directory);

        this.executor = Executors.newFixedThreadPool(threads);
        this.port = port;

        this.serverSocket = socket;

        String image = "image/";
        MIME_TYPES.put(".gif", image + "gif");
        MIME_TYPES.put(".jpg", image + "jpeg");
        MIME_TYPES.put(".jpeg", image + "jpeg");
        MIME_TYPES.put(".png", image + "png");
        String text = "text/";
        MIME_TYPES.put(".html", text + "html");
        MIME_TYPES.put(".htm", text + "html");
        MIME_TYPES.put(".txt", text + "plain");

    }

    public WebServer(String directory, int port, int threads) throws IOException {
        this(new ServerSocket(port), directory, port, threads);
    }

    public void start() throws IOException {
        if(verbose) {
            Logger.log(LogLevel.INFO, "Server started. Listening for connections on port : " + port, getClass());
        }
        while (isRunning) {
            Socket connect = serverSocket.accept();

            if(verbose) {
                Logger.log(LogLevel.INFO, "Connection opened. (" + connect.getInetAddress() + ":" + connect.getPort() + ") Keep-alive " + connect.getKeepAlive(), getClass());
            }

            executor.submit(() -> {
                run(connect);
            });

        }
    }

    public void run(Socket connect) {
        // we manage our particular client connection
        BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            // we read characters from the client via input stream on the socket
            in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            // we get character output stream to client (for headers)
            out = new PrintWriter(connect.getOutputStream());
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(connect.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();
            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            // we get file requested
            fileRequested = parse.nextToken();

            // we support only GET and HEAD methods, we check
            if (!method.equals("GET") && !method.equals("HEAD")) {
                if(verbose) {
                    Logger.log(LogLevel.INFO, "501 Not Implemented : " + method + " method.", getClass());
                }

                // we return the not supported file to the client
                File file = new File(webRoot, methodNotSupported);
                int fileLength = (int) file.length();
                String contentMimeType = "text/html";
                //read content to return to client
                byte[] fileData = readFileData(file, fileLength);

                // we send HTTP Headers with data to client
                out.println("HTTP/1.1 501 Not Implemented");
                out.println("Server: Java HTTP Server from Xilla : 1.0");
                out.println("Date: " + new Date());
                out.println("Content-type: " + contentMimeType);
                out.println("Content-length: " + fileLength);
                out.println(); // blank line between headers and content, very important !
                out.flush(); // flush character output stream buffer
                // file
                dataOut.write(fileData, 0, fileLength);
                dataOut.flush();

            } else {
                // GET or HEAD method
                if (fileRequested.endsWith("/")) {
                    fileRequested += defaultFile;
                }

                File file = new File(webRoot, fileRequested);
                int fileLength = (int) file.length();
                String content = getContentType(fileRequested);

                if (method.equals("GET")) { // GET method so we return content
                    byte[] fileData = readFileData(file, fileLength);

                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    out.println("Server: Java HTTP Server from Xilla : 1.0");
                    out.println("Date: " + new Date());
                    out.println("Content-type: " + content);
                    out.println("Content-length: " + fileLength);
                    out.println(); // blank line between headers and content, very important !
                    out.flush(); // flush character output stream buffer

                    dataOut.write(fileData, 0, fileLength);
                    dataOut.flush();
                }

                if(verbose) {
                    Logger.log(LogLevel.INFO, "File " + fileRequested + " of type " + content + " returned", getClass());
                }
            }

        } catch (FileNotFoundException fnfe) {
            try {
                Logger.log(fnfe, getClass());
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                Logger.log(LogLevel.ERROR, "Error with file not found exception : " + ioe.getMessage(), getClass());
                Logger.log(ioe, getClass());
            }

        } catch (IOException ioe) {
            Logger.log(LogLevel.ERROR, "Server error : " + ioe.getMessage(), getClass());
            Logger.log(ioe, getClass());
        } finally {
            try {
                dataOut.close();
            } catch (Exception e) {
                Logger.log(LogLevel.ERROR, "Error closing stream : " + e.getMessage(), getClass());
                Logger.log(e, getClass());
            }
            try {
                connect.close(); // we close socket connection
            } catch (Exception e) {
                Logger.log(LogLevel.ERROR, "Error closing stream : " + e.getMessage(), getClass());
                Logger.log(e, getClass());
            }
            try {
                out.close();
            } catch (Exception e) {
                Logger.log(LogLevel.ERROR, "Error closing stream : " + e.getMessage(), getClass());
                Logger.log(e, getClass());
            }
            try {
                in.close();
            } catch (Exception e) {
                Logger.log(LogLevel.ERROR, "Error closing stream : " + e.getMessage(), getClass());
                Logger.log(e, getClass());
            }

            if (verbose) {
                Logger.log(LogLevel.ERROR, "Connection closed.", getClass());
            }
        }

    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } catch (Exception ignored) {
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    // return supported MIME Types
    private String getContentType(String fileRequested) {
        for(String ext : MIME_TYPES.keySet()) {
            if(fileRequested.endsWith(ext)) {
                return MIME_TYPES.get(ext);
            }
        }

        return "text/html";
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(webRoot, fileNotFound);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        out.println("Server: Java HTTP Server from Xilla : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();

        if (verbose) {
            Logger.log(LogLevel.ERROR, "File " + fileRequested + " not found.", getClass());
        }
    }

}