package net.xilla.core.library.net;

import lombok.Getter;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.library.net.base.Client;
import net.xilla.core.library.net.base.Server;
import net.xilla.core.library.net.manager.Packet;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.UUID;

public class XillaConnection<T extends Packet> {

    @Getter
    private Client client;
    @Getter
    private Server server;
    @Getter
    private Class<T> clazz;

    public XillaConnection(String name, String ip, int clientPort, int serverPort, Class<T> clazz) throws IOException {
        this.clazz = clazz;

        this.server = new Server(name + "-server", ip, serverPort, (type, address, data) -> {
            Logger.log(LogLevel.DEBUG, "Server: (" + address + ") " + "Sending" + " " + data, getClass());
            ConnectionData<T> connectionData = new ConnectionData<>(data);

            T obj = connectionData.getObject(this);

            if(obj != null) {
                return obj.expectsResponse(this);
            } else  {
                return false;
            }
        }, (type, address, data) -> {
            Logger.log(LogLevel.DEBUG, "Server: (" + address + ") " + "Receiving" + " " + data, getClass());
            ConnectionData<T> connectionData = new ConnectionData<>(data);

            T obj = connectionData.getObject(this);

            if(obj != null) {
                // Checks if it's waiting for a response. If it's not waiting then there's no need to send the response.
                if (connectionData.isResponse() && obj.expectsResponse(this)) {
                    obj.setKey(UUID.randomUUID().toString());
                    ConnectionData<T> cData = new ConnectionData<>(obj, this);
                    return cData.getRaw();
                } else {
                    obj.loadFromDatabase(this);
                }
            }
            return null;
        });

        this.client = new Client(name + "-client", ip, clientPort, (type, address, data) -> {
            Logger.log(LogLevel.DEBUG, "Client: (" + address + ") " + "Sending" + " " + data, getClass());
            ConnectionData<T> connectionData = new ConnectionData<>(data);

            T obj = connectionData.getObject(this);
            if(obj != null) {
                return obj.expectsResponse(this);
            }
            return false;
        }, (type, address, data) -> {
            Logger.log(LogLevel.DEBUG, "Client: (" + address + ") " + "Receiving" + " " + data, getClass());
            ConnectionData<T> connectionData = new ConnectionData<>(data);

            T obj = connectionData.getObject(this);

            if(obj != null) {
                if(obj.expectsResponse(this)){
                    ConnectionData<T> cData = new ConnectionData<>(obj, this);
                    return cData.getRaw();
                }
            }
            return null;
        });
    }

    public void addMessage(ConnectionData data) {
        client.addMessage(data.getRaw());
    }

}
