package net.xilla.core.library.manager;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.XillaLibrary;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;

public abstract class ManagerObject extends XillaLibrary implements SerializedObject {

    @Override
    public XillaJson getSerializedData() {
        XillaJson json = new XillaJson();

        try {
            Class<?> clazz = getClass();
            while (clazz.getSuperclass() != null && !clazz.getSuperclass().getName().equals("ManagerObject")) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.getAnnotation(StoredData.class) != null) {
                        Object object;
                        if (!field.isAccessible()) {
                            field.setAccessible(true);

                            object = field.get(this);

                            field.setAccessible(false);
                        } else {
                            object = field.get(this);
                        }

                        if (object instanceof SerializedObject) {
                            json.put(field.getName(), ((SerializedObject) object).getSerializedData().getJson());
                        } else {
                            json.put(field.getName(), object);
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }

            if(getManager() != null) {
                json.put("key", getKey());
            }

            if(getManager() != null) {
                json.put("manager", getManager().getKey());
            }
            System.out.println(json.getJson());
            return json;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void loadSerializedData(XillaJson json) {
        if(json.containsKey("key")) {
            setKey(json.get("key"));
        }
        if(json.containsKey("manager")) {
            setManager(XillaManager.getInstance().get(json.get("manager")));
        }

        Class<?> clazz = getClass();
        while (clazz.getSuperclass() != null && !clazz.getName().equals("ManagerObject")) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getAnnotation(StoredData.class) != null) {
                    try {
                        Object input = json.get(field.getName());

                        if(input != null) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);

                                Object object = field.get(this);

                                if (field.getType().isAssignableFrom(Long.class)) {
                                    field.set(this, Long.parseLong(input.toString()));
                                } else if (field.getType().isAssignableFrom(Integer.class)) {
                                    field.set(this, Integer.parseInt(input.toString()));
                                } else if (field.getType().isAssignableFrom(Double.class)) {
                                    field.set(this, Double.parseDouble(input.toString()));
                                } else if (field.getType().isAssignableFrom(Float.class)) {
                                    field.set(this, Float.parseFloat(input.toString()));
                                } else if (field.getType().isAssignableFrom(Byte.class)) {
                                    field.set(this, Byte.parseByte(input.toString()));
                                } else if (field.getType().isAssignableFrom(Short.class)) {
                                    field.set(this, Short.parseShort(input.toString()));
                                } else if (object instanceof SerializedObject) {
                                    SerializedObject serializedObject = ((SerializedObject) object);
                                    serializedObject.loadSerializedData(new XillaJson((JSONObject)input));
                                } else {
                                    try {
                                        field.set(this, input);
                                    } catch (Exception ex) {
                                        Logger.log(LogLevel.ERROR, "Failed to load variable " + field.getName() + "!", getClass());
                                        Logger.log(ex, getClass());
                                    }
                                }

                                field.setAccessible(false);
                            } else {
                                Object object = field.get(this);

                                if (field.getType().isAssignableFrom(Long.class)) {
                                    field.set(this, Long.parseLong(input.toString()));
                                } else if (field.getType().isAssignableFrom(Integer.class)) {
                                    field.set(this, Integer.parseInt(input.toString()));
                                } else if (field.getType().isAssignableFrom(Double.class)) {
                                    field.set(this, Double.parseDouble(input.toString()));
                                } else if (field.getType().isAssignableFrom(Float.class)) {
                                    field.set(this, Float.parseFloat(input.toString()));
                                } else if (field.getType().isAssignableFrom(Byte.class)) {
                                    field.set(this, Byte.parseByte(input.toString()));
                                } else if (field.getType().isAssignableFrom(Short.class)) {
                                    field.set(this, Short.parseShort(input.toString()));
                                } else if (field.getType().isAssignableFrom(SerializedObject.class)) {
                                    if (object == null) {
                                        Logger.log(LogLevel.ERROR, "To load a serialized object, it must already be initialized.", getClass());
                                    } else {
                                        SerializedObject serializedObject = ((SerializedObject) object);
                                        serializedObject.loadSerializedData(new XillaJson((JSONObject)input));
                                    }
                                } else {
                                    field.set(this, input);
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            clazz = clazz.getSuperclass();
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
