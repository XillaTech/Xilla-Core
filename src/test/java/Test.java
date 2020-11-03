import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.library.net.manager.PacketManager;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    private PacketManager manager = PacketManager.getInstance();

    public Test() {
        Logger.setLogLevel(LogLevel.DEBUG);

        System.out.println("Fields");

        for(Field field : ManagerObject.class.getFields()) {
            System.out.println(field.getName());
            for(Annotation ann : field.getDeclaredAnnotations()) {
                System.out.println(" > " + ann.annotationType().getName());
            }
            for(Annotation ann : field.getAnnotations()) {
                System.out.println(" > " + ann.annotationType().getName());
            }
        }

        System.out.println("Declared Fields");

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
