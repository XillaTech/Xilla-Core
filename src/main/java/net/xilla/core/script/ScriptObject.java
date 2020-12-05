package net.xilla.core.script;

import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;

public class ScriptObject implements SerializedObject {

    private Object data;

    public ScriptObject(Object object) {
        this.data = object;
    }

    @Override
    public XillaJson getSerializedData() {
        if(data instanceof SerializedObject) {
            return ((SerializedObject)data).getSerializedData();
        }
        return new XillaJson().put("raw-data", data);
    }

    @Override
    public void loadSerializedData(XillaJson json) {

    }
}
