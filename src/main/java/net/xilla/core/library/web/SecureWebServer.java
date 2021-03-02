package net.xilla.core.library.web;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SecureWebServer extends WebServer {

    public SecureWebServer(String directory, String trustStore, String password, int port, int threads) throws IOException {
        super(SSLServerSocketFactory.getDefault().createServerSocket(port), directory, port, threads);
        System.setProperty("javax.net.ssl.trustStore", trustStore);
        System.setProperty("javax.net.ssl.keyStorePassword", password);
    }

}