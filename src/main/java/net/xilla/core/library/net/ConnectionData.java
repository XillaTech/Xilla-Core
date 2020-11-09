package net.xilla.core.library.net;

import lombok.Getter;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.net.manager.packet.Packet;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ConnectionData<T extends Packet> extends XillaJson {

    private String raw;
    @Getter
    private boolean response;
    @Getter
    private XillaJson data;

    public ConnectionData(String raw) {
        this.raw = raw;
        parse(raw);
        response = get("response");
        data = new XillaJson(get("data"));

    }

    public ConnectionData(Packet object, XillaConnection<T> connection) {
        XillaJson json = new XillaJson();
        json.put("response", object.expectsResponse(connection));
        json.put("manager", object.getManager().getName());
        json.put("data", object.loadFromDatabase(connection).getJson());
        this.raw = json.toJSONString();
        parse(raw);
        this.response = object.expectsResponse(connection);
        this.data = object.getSerializedData();
    }

    public String getRaw() {
        return raw;
    }

    public T getObject(XillaConnection<T> connection) {
        Constructor<?>[] constructors = connection.getClazz().getConstructors();
        for (Constructor<?> c : constructors) {
            if (c.getParameterTypes().length == 0) {
                try {
                    T obj = (T)c.newInstance();
                    obj.loadSerializedData(getData());
                    Logger.log(LogLevel.DEBUG, "Loaded object " + obj.getKey() + " - " + obj.getSerializedData().toJSONString(), getClass());
                    return obj;
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                }
                break;
            } else if (c.getParameterTypes().length == 1) {
                try {
                    T obj = (T) c.newInstance(getData().getJson());
                    obj.loadSerializedData(getData());
                    Logger.log(LogLevel.DEBUG, "Loaded object " + obj.getKey() + " - " + obj.getSerializedData().toJSONString(), getClass());
                    return obj;
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                }
                break;
            }
        }
        return null;
    }

}
