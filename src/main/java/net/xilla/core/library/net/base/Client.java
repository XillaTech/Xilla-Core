package net.xilla.core.library.net.base;

import net.xilla.core.library.worker.Worker;
import net.xilla.core.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;

public abstract class Client extends Worker {

    private Socket socket;
    private LinkedList<String> messages = new LinkedList<>();
    private ExecutorService pool;

    public Client(String name, String ip, int port) throws IOException {
        super(name, -1);

        if (ip != null && !ip.isEmpty()) {
            this.socket = new Socket(InetAddress.getByName(ip), port);
        } else {
            this.socket = new Socket(InetAddress.getLocalHost(), port);
        }
    }

    @Override
    public void runWorker(long start) {
        boolean shouldWait = false;
        try {
            if (messages.size() > 0) {
                String input = messages.remove(0);
                PrintWriter out = null;
                try {
                    out = new PrintWriter(this.socket.getOutputStream(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (out != null) {
                    out.println(input);
                    shouldWait = messageSent(socket.getInetAddress().getHostAddress(), input);
                    out.flush();
                }
            }
        } catch (Exception ex) {
            Logger.log(ex, getClass());
        }

        if(shouldWait) {
            try {
                String data;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                while ((data = in.readLine()) != null) {
                    addMessage(messageReceived(socket.getInetAddress().getHostAddress(), data));
                    break;
                }
            } catch (Exception ex) {
                Logger.log(ex, getClass());
            }
        }
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public abstract boolean messageSent(String ip, String input);

    public abstract String messageReceived(String ip, String input);

}
