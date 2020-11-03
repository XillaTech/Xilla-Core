package net.xilla.core.library.net.base;

import net.xilla.core.library.net.ResponseType;
import net.xilla.core.library.worker.Worker;
import net.xilla.core.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public abstract class Server extends Worker {

    private ServerSocket server;
    private boolean running;
    private byte[] buf = new byte[256];

    public Server(String name, String ip, int port) throws IOException {
        super(name, -1);

        if (ip != null && !ip.isEmpty()) {
            this.server = new ServerSocket(port, 1, InetAddress.getByName(ip));
        } else {
            this.server = new ServerSocket(port, 1, InetAddress.getLocalHost());
        }
    }

    @Override
    public void runWorker(long start) {
        try {
            listen();
        } catch (Exception ex) {
            Logger.log(ex, getClass());
        }
    }

    private void listen() throws Exception {
        String data = null;
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream()));
        while ( (data = in.readLine()) != null ) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (out != null) {
                String input = messageReceived(getSocketAddress().getHostAddress(), data);
                if(input != null && !input.isEmpty()) {
                    messageSent(getSocketAddress().getHostAddress(), input);
                    out.println(input);
                    out.flush();
                }
            }
        }
    }
    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }

    public abstract boolean messageSent(String ip, String input);

    public abstract String messageReceived(String ip, String input);

}
