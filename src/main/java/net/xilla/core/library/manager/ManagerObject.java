package net.xilla.core.library.manager;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.XillaLibrary;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import net.xilla.core.reflection.Reflection;
import net.xilla.core.reflection.ReflectionManager;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;

public abstract class ManagerObject implements XillaLibrary, SerializedObject {

    @Setter
    @Getter
    private Object key;

    @Getter
    @Setter
    private Manager<Object, ManagerObject> manager;

    @Getter
    @Setter
    private String extension = "none";

    public ManagerObject() {
    }

    public ManagerObject(Object key, Manager manager) {
        init(key, manager);
    }

    public ManagerObject(Object key, String manager) {
        init(key, manager);
    }

    public void init(Object key, String manager) {
        init(key, XillaManager.getInstance().get(manager));
    }

    public void init(Object key, Manager manager) {
        setKey(key);
        setManager(manager);

        if(manager != null && manager.getConfig() != null) {
            this.extension = manager.getConfig().getConfigFile().getExtension();
        }

        postSetup();
    }

    public void postSetup() {

    }

    public void updateKey(String key) {
        this.key = key;
        manager.getData().remove(key);
        manager.getData().put(key, this);
    }

    @Override
    public void loadSerializedData(XillaJson json) {

        if(json.containsKey("file-extension")) {
            json.getJson().remove("file-extension");
        }

        Class<?> clazz = getClass();
        while (clazz.getSuperclass() != null && !clazz.getName().equals("ManagerObject")) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getAnnotation(StoredData.class) != null) {
                    Object input = json.get(field.getName());

                    if(input != null) {

                        Reflection reflection = ReflectionManager.getInstance().get(field.getType());

                        boolean locked = false;
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                            locked = true;
                        }

                        try {
                            Object obj = field.get(this);
                            if(obj instanceof SerializedObject) {
                                reflection = ReflectionManager.getInstance().get(SerializedObject.class);
                            }

                            if(reflection != null) {
                                try {
                                    field.set(this, reflection.loadFromSerializedData(this, field, input));
                                } catch (Exception ex) {
                                    Logger.log(LogLevel.ERROR, "Failed to set variable " + field.getName(), getClass());
                                }
                            }
                            return;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }



                        try {
                            field.set(this, input);
                        } catch (Exception ex) {
                            Logger.log(LogLevel.ERROR, "Failed to load variable " + field.getName(), getClass());
                        }


                        if(locked) {
                            field.setAccessible(false);
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        if(json.containsKey("key") && json.containsKey("manager")) {
            init(json.get("key"), json.get("manager").toString());
        }
    }

    @Override
    public XillaJson getSerializedData() {
        XillaJson json = new XillaJson();

        try {
            Class<?> clazz = getClass();
            while (clazz.getSuperclass() != null && !clazz.getSuperclass().getName().equals("ManagerObject")) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.getAnnotation(StoredData.class) != null) {
                        Reflection reflection = ReflectionManager.getInstance().get(field.getType());

                        boolean locked = false;
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                            locked = true;
                        }

                        try {
                            Object obj = field.get(this);
                            if(obj instanceof SerializedObject) {
                                reflection = ReflectionManager.getInstance().get(SerializedObject.class);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        Object object = field.get(this);
                        if(object != null) {
                            if (reflection != null) {
                                json.put(field.getName(), reflection.getSerializedData(this, field, object));
                            } else {
                                json.put(field.getName(), object);
                            }
                        }

                        if(locked) {
                            field.setAccessible(false);
                        }

                    }
                }
                clazz = clazz.getSuperclass();
            }

            if(manager != null) {
                if (manager.isContainsIdentifiers()) {
                    if (getKey() != null) {
                        json.put("key", getKey());
                    }

                    if (getManager() != null) {
                        json.put("manager", getManager().getKey());
                    }
                }
            }

            if(json.containsKey("file-extension")) {
                json.getJson().remove("file-extension");
            }

            return json;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return getKey().toString();
    }
}
