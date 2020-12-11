package net.xilla.core.testmanager;

import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.library.manager.StoredData;

public class TestObject extends ManagerObject {

    @StoredData
    private Integer data = 0;

    public TestObject(String name) {
        super(name, "Test");
    }

    public TestObject() {}

}
