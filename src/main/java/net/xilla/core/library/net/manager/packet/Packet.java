package net.xilla.core.library.net.manager.packet;

import lombok.Getter;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.library.net.XillaConnection;

public abstract class Packet extends ManagerObject {

    @Getter
    private String content;

    public Packet(String key, String content) {
        super(key, "PacketManager");

        this.content = content;
    }


    public Packet() {
        super("", "PacketManager");
    }

    @Override
    public XillaJson getSerializedData() {
        XillaJson json = new XillaJson();

        json.put("key", getKey());
        json.put("content", content);

        return json;
    }

    @Override
    public void loadSerializedData(XillaJson json) {
        setKey(json.get("key"));

        this.content = json.get("content");
    }

    public abstract XillaJson loadFromDatabase(XillaConnection connection);

    public abstract boolean expectsResponse(XillaConnection connection);

}
