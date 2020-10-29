import net.xilla.core.library.net.ConnectionData;
import net.xilla.core.library.net.XillaConnection;
import net.xilla.core.library.net.manager.PacketManager;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import net.xilla.core.library.net.manager.Packet;
import server.TestPacket;

import java.io.IOException;
import java.util.UUID;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    private PacketManager manager = PacketManager.getInstance();

    public Test() {
        Logger.setLogLevel(LogLevel.DEBUG);
        try {
            XillaConnection<TestPacket> xillaConnection = new XillaConnection<>("Test", null, 732, 732, TestPacket.class);
            Logger.log(LogLevel.INFO, "Starting the server...", Test.class);
            xillaConnection.getServer().start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            Logger.log(LogLevel.INFO, "Starting the client...", Test.class);
            xillaConnection.getClient().start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            TestPacket object = new TestPacket(UUID.randomUUID().toString(), "Test data, woah.");

            Logger.log(LogLevel.INFO, "Sending test object " + object.getKey() + " - " + object.getSerializedData().toJSONString(), Test.class);

            xillaConnection.addMessage(new ConnectionData<>(object, xillaConnection));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
