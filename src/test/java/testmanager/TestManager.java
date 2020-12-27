package testmanager;

import net.xilla.core.library.manager.Manager;

public class TestManager extends Manager<String, TestObject> {

    public static void main(String[] args) {
        new TestManager();
    }

    public TestManager() {
        super("Test", "test-folder.jsonf", TestObject.class);
        load();

        if(!containsKey("test1")) {
            put(new TestObject("test1"));
        }
        if(!containsKey("test2")) {
            put(new TestObject("test2"));
        }
        if(!containsKey("test3")) {
            put(new TestObject("test3"));
        }

        save();
    }

}
