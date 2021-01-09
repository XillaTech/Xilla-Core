package testmanager;

import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.library.manager.StoredData;

import java.util.ArrayList;
import java.util.List;

public class TestObject extends ManagerObject {

    @StoredData
    private Integer data = 0;

    private List<Integer> list = new ArrayList<>();

    public TestObject(String name) {
        super(name, "Test");
    }

    public TestObject() {}

    @Override
    public void loadSerializedData(XillaJson json) {
        super.loadSerializedData(json);

        for(Object obj : (List<Object>)json.get("list")) {
            list.add(Integer.parseInt(obj.toString()));
        }
    }

    @Override
    public XillaJson getSerializedData() {
        XillaJson json = super.getSerializedData();

        json.put("list", list);

        return json;
    }
}
