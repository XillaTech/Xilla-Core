package net.xilla.core;

import net.xilla.core.script.Script;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    public Test() {

        try {
            new Script("test_script.xs").run();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            XillaConnection<TestPacket> xillaConnection = new XillaConnection<>("net.xilla.core.Test", null, 732, 732, TestPacket.class);
//            Logger.log(LogLevel.INFO, "Starting the server...", net.xilla.core.Test.class);
//            xillaConnection.getServer().start();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//
//            Logger.log(LogLevel.INFO, "Starting the client...", net.xilla.core.Test.class);
//            xillaConnection.getClient().start();
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//
//            TestPacket object = new TestPacket(UUID.randomUUID().toString(), "net.xilla.core.Test data, woah.");
//
//            Logger.log(LogLevel.INFO, "Sending test object " + object.getKey() + " - " + object.getSerializedData().toJSONString(), net.xilla.core.Test.class);
//
//            xillaConnection.addMessage(new ConnectionData<>(object, xillaConnection));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
