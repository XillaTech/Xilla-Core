package net.xilla.core.library.manager;

import lombok.Getter;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.XillaLibrary;

import java.lang.reflect.Field;

public abstract class ManagerObject extends XillaLibrary implements SerializedObject {

    @Override
    public XillaJson getSerializedData() {
        XillaJson json = new XillaJson();

        Class<?> c = this.getClass();

        try {
            for(Field field : getClass().getDeclaredFields()) {
                if (field.getAnnotation(StoredData.class) != null) {
                    json.put(field.getName(), field.get(this));
                }
            }
            return json;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void loadSerializedData(XillaJson json) {
        for(Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(StoredData.class) != null) {
                if(json.containsKey(field.getName())) {
                    try {
                        field.set(this, json.get(field.getName()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Getter
    @StoredData
    private String key;

    @Getter
    private Manager manager;

    public ManagerObject() {

    }

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
