package net.xilla.core.library.manager;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.XillaLibrary;

import java.lang.reflect.Field;

public abstract class ManagerObject extends XillaLibrary implements SerializedObject {

    @Override
    public XillaJson getSerializedData() {
        XillaJson json = new XillaJson();
        try {
            for(Field field : getClass().getDeclaredFields()) {
                if (field.getAnnotation(StoredData.class) != null) {
                    Object object;
                    if(!field.isAccessible()) {
                        field.setAccessible(true);

                        object = field.get(this);

                        field.setAccessible(false);
                    } else {
                        object = field.get(this);
                    }

                    if(object instanceof SerializedObject) {
                        json.put(field.getName(), getSerializedData().getJson());
                    }
                }
            }
            json.put("key", getKey());
            json.put("manager", getManager().getKey());
            return json;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void loadSerializedData(XillaJson json) {
        setKey(json.get("key"));
        if(json.containsKey("manager")) {
            manager = XillaManager.getInstance().get(json.get("manager"));
        }
        for(Field field : getClass().getDeclaredFields()) {
            if (field.getAnnotation(StoredData.class) != null) {
                if(json.containsKey(field.getName())) {
                    try {
                        if(!field.isAccessible()) {
                            field.setAccessible(true);

                            Object object = field.get(this);

                            if(object instanceof SerializedObject) {
                                ((SerializedObject) object).loadSerializedData(new XillaJson(json.get(field.getName())));
                            } else {
                                field.set(this, json.get(field.getName()));
                            }
                            field.setAccessible(false);
                        } else {
                            Object object = field.get(this);

                            if(object instanceof SerializedObject) {
                                ((SerializedObject) object).loadSerializedData(new XillaJson(json.get(field.getName())));
                            } else {
                                field.set(this, json.get(field.getName()));
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Setter
    @Getter
    private String key;

    @Getter
    @Setter
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

}
