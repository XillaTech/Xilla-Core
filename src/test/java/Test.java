import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.library.net.manager.packet.PacketManager;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    private PacketManager manager = PacketManager.getInstance();

    private static boolean booleanTest = true;
    private volatile boolean ATOMIC_TEST = true;

    private static final double DIVIDER = 1;

    public Test() {

        long start = System.currentTimeMillis();

        for (int i = 0; i < 100000000; i++) {
            booleanTest = !booleanTest;
        }

        long time = System.currentTimeMillis() - start;

        System.out.println("Took " + (time / DIVIDER) + "ms");

        start = System.currentTimeMillis();

        for (int i = 0; i < 100000000; i++) {
            ATOMIC_TEST = !ATOMIC_TEST;
        }

        time = System.currentTimeMillis() - start;

        System.out.println("Took " + (time / DIVIDER) + "ms");

//        try {
//            XillaConnection<TestPacket> xillaConnection = new XillaConnection<>("Test", null, 732, 732, TestPacket.class);
//            Logger.log(LogLevel.INFO, "Starting the server...", Test.class);
//            xillaConnection.getServer().start();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//
//            Logger.log(LogLevel.INFO, "Starting the client...", Test.class);
//            xillaConnection.getClient().start();
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//
//            TestPacket object = new TestPacket(UUID.randomUUID().toString(), "Test data, woah.");
//
//            Logger.log(LogLevel.INFO, "Sending test object " + object.getKey() + " - " + object.getSerializedData().toJSONString(), Test.class);
//
//            xillaConnection.addMessage(new ConnectionData<>(object, xillaConnection));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
