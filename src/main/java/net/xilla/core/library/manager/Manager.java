package net.xilla.core.library.manager;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.config.Config;
import net.xilla.core.library.config.ConfigSection;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Manager<Key, Value extends ObjectInterface> extends ManagerObject {

    @Getter
    @Setter
    private String name;

    @Getter
    private Config config = null;

    @Setter
    @Getter
    private boolean containsIdentifiers = false;

    @Getter
    private final Map<Key, Value> data = new ConcurrentHashMap<>();

    private Class<Value> clazz = null;

    public Manager(String name) {
        super(name, XillaManager.getInstance());
        this.name = name;
    }

    public Manager(String name, String file) {
        this(name);
        this.config = new Config(file);

        if(!(this instanceof XillaManager)) {
            XillaManager.getInstance().put(this);
        }
    }

    public Manager(String name, Class<Value> clazz) {
        this(name);
        this.clazz = clazz;
    }

    public Manager(String name, String file, Class<Value> clazz) {
        this(name, file);
        this.clazz = clazz;
    }

    public void save() {
        if(config != null) {
            for (Value object : data.values()) {
                try {
                    XillaJson json = object.getSerializedData();
                    if(json == null) {
                        throw new Exception("Manager Object is missing json data!");
                    }
                    config.set(object.getKey().toString(), json.getJson());
                } catch (Exception ex) {
                    Logger.log(LogLevel.ERROR, "Error while saving item " + object.getKey() + "!", getClass());
                    ex.printStackTrace();
                }
            }
            config.save();
        } else {
            Logger.log(LogLevel.ERROR, "You cannot save the manager without setting a config file!", this.getClass());
        }
    }

    protected void load() {
        config.reload();
        if(clazz != null) {
            XillaJson json = getConfig().getJson();

            json.remove("file-extension");

            for (Object key : json.getJson().keySet()) {
                Object data = config.getConfigFile().getIndex().get(key.toString());

                if(data instanceof Boolean) {
                    Boolean b = (Boolean) data;

                    if(b) {
                        loadItem((Key)key);
                    }
                } else {
                    loadItem((Key)key);
                }

            }
        }
    }

    protected void loadItem(Key key) {
        ConfigSection section = getConfig().getConfigFile().getSection(key.toString());

        Value object = get(key);
        if (object == null) {
            object = getObject(key, section.getJson());
            if (object == null) {
                Logger.log(LogLevel.ERROR, "No valid constructor found for objects in manager " + getName(), getClass());
            } else {
                put(object);
            }
        } else {
            object.loadSerializedData(section.getJson());
            if(object.getKey() == null) {
                object.setKey(key);
            }
            if(object.getManager() == null) {
                object.setManager((Manager<Object, ManagerObject>) this);
            }
            remove(object);
            put(object);
        }
    }

    protected void objectAdded(Value obj) {}

    protected void objectRemoved(Value obj) {}

    public void put(Value object) {

        if(object == null) {
            Logger.log(LogLevel.FATAL, "CANNOT INPUT NULL OBJECT!", getClass());
            Logger.log(new Exception("CANNOT INPUT NULL OBJECT!"), getClass());
            return;
        }

        if(object.getKey() == null) {
            Logger.log(LogLevel.FATAL, "Object has a null key!", getClass());
            Logger.log(new Exception("Object has a null key!"), getClass());
            return;
        }

        objectAdded(object);

        if(config != null) {
            config.load(object.getKey().toString());
        }

        this.data.put((Key)object.getKey(), object);
    }

    public void remove(Value object) {
        objectRemoved(object);

        if(config != null) {
            config.getConfigFile().remove(object.getKey().toString());
        }

        this.data.remove(object.getKey());
    }

    public void removeKey(Key key) {
        objectRemoved(get(key));

        if(config != null) {
            config.getConfigFile().remove(get(key).getKey().toString());
        }

        this.data.remove(key);
    }

    public Value get(Key key) {
        Value obj = this.data.get(key);

        if(obj != null) {
            return obj;
        }

        return null;
    }

    public XillaJson getSerializedData() {
        return null;
    }

    public void loadSerializedData(XillaJson json) {

    }

    public String toString() {
        if(getSerializedData() == null) {
            return super.toString();
        } else {
            return getSerializedData().toJSONString();
        }
    }

    protected Value getObject(Key key, XillaJson json) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> c : constructors) {
            if (c.getParameterTypes().length == 0) {
                try {
                    Value obj = (Value)c.newInstance();

                    json.put("key", key);
                    json.put("manager", this.getKey());
                    json.put("file-extension", config.getConfigFile().getExtension());

                    obj.loadSerializedData(json);

                    if(obj.getKey() == null) {
                        obj.setKey(key);
                    }
                    if(obj.getManager() == null) {
                        obj.setManager((Manager<Object, ManagerObject>) this);
                    }
//                    Logger.log(LogLevel.DEBUG, "Loaded object " + obj.getKey() + " - " + obj.getSerializedData().toJSONString(), getClass());
                    return obj;
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public List<Value> iterate() {
        return new LinkedList<>(data.values());
    }

    public int size() {
        return getData().size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(Key key) {
        return getConfig().getConfigFile().contains(key.toString());
    }

    @Deprecated
    public boolean containsValue(Value value) {
        return data.containsValue(value);
    }

    public Value put(Key key, Value value) {
        objectAdded(value);
        return data.put(key, value);
    }

    public Value remove(Key key) {
        objectRemoved(data.get(key));
        return data.remove(key);
    }

    public void putAll(Map<? extends Key, ? extends Value> m) {
        data.putAll(m);
        m.forEach(((key, value) -> objectAdded(value)));
    }

    public void clear() {
        data.forEach(((key, value) -> objectRemoved(value)));
        data.clear();
    }

    public boolean load(Key key) {
        if(config != null) {
            if (config.load(key.toString())) {
                loadItem(key);
                return true;
            }
        }
        return false;
    }

    public boolean unload(Key key) {
        if(config != null) {
            if (config.unload(key.toString())) {
                data.remove(key);
                return true;
            }
        }
        return false;
    }

    public Set<Key> keySet() {
        return data.keySet();
    }

    public Collection<Value> values() {
        return data.values();
    }

    public Set<Map.Entry<Key, Value>> entrySet() {
        return data.entrySet();
    }

}
