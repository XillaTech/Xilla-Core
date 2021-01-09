package net.xilla.core.library.manager;

import net.xilla.core.library.XillaLibrary;
import net.xilla.core.library.json.SerializedObject;
import org.json.simple.JSONAware;

public interface ObjectInterface extends SerializedObject, XillaLibrary, JSONAware {

    void setKey(Object obj);

    Object getKey();

    void setManager(Manager<Object, ManagerObject> manager);

    Manager<Object, ManagerObject> getManager();

    @Override
    default String toJSONString() {
        return getSerializedData().getJson().toJSONString();
    }

}
