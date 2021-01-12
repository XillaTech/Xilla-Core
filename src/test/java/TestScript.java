import net.xilla.core.script.Script;

public class TestScript {

    public static void main(String[] args) {
        new TestScript();
    }

    public TestScript() {

        try {
            new Script("test_script.xs").run();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
