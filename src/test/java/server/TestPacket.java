package server;

import com.sun.tools.javac.Main;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.net.XillaConnection;
import net.xilla.core.library.net.manager.Packet;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.util.UUID;
import java.util.function.Supplier;

public class TestPacket extends Packet {

    private long lastUpdated;

    public TestPacket(String key, String content) {
        super(key, content);
    }

    public TestPacket() {
    }

    @Override
    public XillaJson loadFromDatabase(XillaConnection connection) {
        Logger.log(LogLevel.INFO, "Catching the test object load", getClass());
        String content = getContent();
        if(content.length() < 100) {
            Logger.log(LogLevel.INFO, "Returning a new object", getClass());
            content = content + "A";
            return new TestPacket(UUID.randomUUID().toString(), content).getSerializedData();
        }
        return null;
    }

    @Override
    public boolean expectsResponse(XillaConnection connection) {
        String content = getContent();
        return content.length() < 100 - 1;
    }

}
