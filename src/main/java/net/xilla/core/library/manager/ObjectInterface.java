package net.xilla.core.library.manager;

import net.xilla.core.library.XillaLibrary;
import net.xilla.core.library.json.SerializedObject;

public interface ObjectInterface extends SerializedObject, XillaLibrary {

    void setKey(Object obj);

    Object getKey();

    void setManager(Manager<Object, ManagerObject> manager);

    Manager<Object, ManagerObject> getManager();

}
