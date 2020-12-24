package testmanager;

import net.xilla.core.library.manager.Manager;

public class TestManager extends Manager<String, TestObject> {

    public static void main(String[] args) {
        new TestManager();
    }

    public TestManager() {
        super("Test", "test-file.json", TestObject.class);
        load();

        if(!containsKey("test")) {
            put(new TestObject("test"));
        }

        save();
    }

}
