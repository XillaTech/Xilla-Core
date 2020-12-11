package net.xilla.core.testmanager;

import net.xilla.core.library.manager.Manager;
import net.xilla.core.reflection.ReflectionManager;

public class TestManager extends Manager<String, TestObject> {

    public static void main(String[] args) {
        new TestManager();
    }

    public TestManager() {
        super("Test", "test-file.json", TestObject.class);
        load();

        put(new TestObject("test"));

        save();
    }

}
