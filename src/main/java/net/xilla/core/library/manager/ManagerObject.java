package net.xilla.core.library.manager;

import lombok.Getter;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.XillaLibrary;
import net.xilla.core.library.net.XillaConnection;

public abstract class ManagerObject extends XillaLibrary implements SerializedObject {

    @Override
    public abstract XillaJson getSerializedData();

    @Override
    public abstract void loadSerializedData(XillaJson json);

    @Getter
    private String key;

    @Getter
    private Manager manager;

    public ManagerObject(String key, Manager manager) {
        this.key = key;
        this.manager = manager;
    }

    public ManagerObject(String key, String manager) {
        this.key = key;
        this.manager = XillaManager.getInstance().get(manager);
    }

    public void updateKey(String key) {
        this.key = key;
        manager.getData().remove(key);
        manager.getData().put(key, this);
    }

    public void setKey(String key) {
        this.key = key;
    }

}
