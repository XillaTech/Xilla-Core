package net.xilla.core.library.manager;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.config.Config;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.net.XillaConnection;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import org.json.simple.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Manager<T extends ManagerObject> extends ManagerObject {

    @Getter
    @Setter
    private String name;

    @Getter
    private Config config = null;

    @Getter
    private Map<String, T> data;

    @Getter
    private Map<String, ManagerCache<T>> cacheList;

    private Class<T> clazz = null;

    public Manager(String name) {
        super(name, XillaManager.getInstance());
        this.name = name;
        this.data = new ConcurrentHashMap<>();
        this.cacheList = new ConcurrentHashMap<>();

        if(!(this instanceof XillaManager)) {
            XillaManager.getInstance().put(this);
        }
    }

    public Manager(String name, String file) {
        this(name);
        this.config = new Config(file);
    }

    public Manager(String name, Class<T> clazz) {
        super(name, XillaManager.getInstance());
        this.name = name;
        this.data = new ConcurrentHashMap<>();
        this.cacheList = new ConcurrentHashMap<>();

        this.clazz = clazz;

        if(!(this instanceof XillaManager)) {
            XillaManager.getInstance().put(this);
        }
    }

    public Manager(String name, String file, Class<T> clazz) {
        this(name);
        this.config = new Config(file);

        this.clazz = clazz;
    }

    public ManagerCache<T> getCache(String key) {
        if(!cacheList.containsKey(key)) {
            cacheList.put(key, new ManagerCache<T>());
        }
        return cacheList.get(key);
    }

    public void save() {
        if(config != null) {
            config.clear();
            for (T object : data.values()) {
                config.set(object.getKey(), object.getSerializedData().getJson());
            }
            config.save();
        } else {
            Logger.log(LogLevel.ERROR, "You cannot save the manager without setting a config file!", this.getClass());
        }
    }

    protected void load() {
        if(clazz != null) {
            for (Object obj : getConfig().getJson().getJson().values()) {
                JSONObject json = (JSONObject) obj;
                T object = getObject(new XillaJson(json));
                System.out.println(object.getSerializedData());
                put(object);
            }
        }
    }

    protected abstract void objectAdded(T obj);

    protected abstract void objectRemoved(T obj);

    public void put(T object) {
        objectAdded(object);
        this.data.put(object.getKey(), object);
    }

    public void remove(T object) {
        objectRemoved(object);
        this.data.remove(object.getKey());
    }

    public void remove(String key) {
        objectRemoved(get(key));
        this.data.remove(key);
    }

    public T get(String key) {
        T obj = this.data.get(key);

        if(obj != null) {
            return obj;
        }

        for (ManagerCache<T> cache : new ConcurrentHashMap<>(cacheList).values()) {
            obj = cache.getObject(key);
            if(obj != null) {
                return obj;
            }
        }

        return null;
    }

    @Override
    public XillaJson getSerializedData() {
        return null;
    }

    @Override
    public void loadSerializedData(XillaJson json) {

    }

    @Override
    public String toString() {
        if(getSerializedData() == null) {
            return super.toString();
        } else {
            return getSerializedData().toJSONString();
        }
    }

    public T getObject(XillaJson json) {
        System.out.println("A");
        Constructor<?>[] constructors = clazz.getConstructors();
        System.out.println("B");
        for (Constructor<?> c : constructors) {
            System.out.println("C");
            if (c.getParameterTypes().length == 0) {
                System.out.println("D");
                try {
                    System.out.println("E");
                    T obj = (T)c.newInstance();
                    System.out.println("F1");
                    obj.loadSerializedData(json);
                    System.out.println("G");
                    System.out.println("G " + obj.getSerializedData().getJson());
                    Logger.log(LogLevel.DEBUG, "Loaded object " + obj.getKey() + " - " + obj.getSerializedData().toJSONString(), getClass());
                    return obj;
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }
}
