package net.xilla.core.library.net;

import lombok.Getter;
import net.xilla.core.library.net.base.Client;
import net.xilla.core.library.net.base.Server;
import net.xilla.core.library.net.manager.packet.Packet;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.io.IOException;
import java.util.UUID;

public class XillaConnection<T extends Packet> {

    @Getter
    private Client client;
    @Getter
    private Server server;
    @Getter
    private Class<T> clazz;

    private final XillaConnection<T> instance;

    public XillaConnection(String name, String ip, int clientPort, int serverPort, Class<T> clazz) throws IOException {
        this.clazz = clazz;

        this.instance = this;

        this.server = new Server(name + "-server", ip, serverPort) {
            @Override
            public boolean messageSent(String ip, String input) {
                Logger.log(LogLevel.DEBUG, "Server: (" + ip + ") " + "Sending" + " " + input, getClass());
                ConnectionData<T> connectionData = new ConnectionData<>(input);

                Packet obj = connectionData.getObject(instance);

                if(obj != null) {
                    return obj.expectsResponse(instance);
                } else  {
                    return false;
                }
            }

            @Override
            public String messageReceived(String ip, String input) {
                Logger.log(LogLevel.DEBUG, "Server: (" + ip + ") " + "Receiving" + " " + input, getClass());
                ConnectionData<T> connectionData = new ConnectionData<>(input);

                Packet obj = connectionData.getObject(instance);

                if(obj != null) {
                    // Checks if it's waiting for a response. If it's not waiting then there's no need to send the response.
                    if (connectionData.isResponse() && obj.expectsResponse(instance)) {
                        obj.setKey(UUID.randomUUID().toString());
                        ConnectionData<T> cData = new ConnectionData<>(obj, instance);
                        return cData.getRaw();
                    } else {
                        obj.loadFromDatabase(instance);
                    }
                }
                return null;
            }
        };


        this.client = new Client(name + "-client", ip, clientPort) {
            @Override
            public boolean messageSent(String ip, String input) {
                    Logger.log(LogLevel.DEBUG, "Client: (" + ip + ") " + "Sending" + " " + input, getClass());
                    ConnectionData<T> connectionData = new ConnectionData<>(input);

                    Packet obj = connectionData.getObject(instance);
                    if(obj != null) {
                        return obj.expectsResponse(instance);
                    }
                    return false;
            }

            @Override
            public String messageReceived(String ip, String input) {
                Logger.log(LogLevel.DEBUG, "Client: (" + ip + ") " + "Receiving" + " " + input, getClass());
                ConnectionData<T> connectionData = new ConnectionData<>(input);

                Packet obj = connectionData.getObject(instance);

                if(obj != null) {
                    if(obj.expectsResponse(instance)){
                        ConnectionData<T> cData = new ConnectionData<>(obj, instance);
                        return cData.getRaw();
                    }
                }
                return null;
            }
        };
    }

    public void addMessage(ConnectionData data) {
        client.addMessage(data.getRaw());
    }

}
