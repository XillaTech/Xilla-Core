package net.xilla.core.library.manager;

import lombok.Getter;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.XillaLibrary;

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
        System.out.println(manager);
        System.out.println(this.manager);
        System.out.println(XillaManager.getInstance().getData().keySet());
    }

    public void setKey(String key) {
        this.key = key;
        manager.getData().remove(key);
        manager.getData().put(key, this);
    }

}
