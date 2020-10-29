package net.xilla.core.library.manager;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.config.Config;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.net.XillaConnection;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

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

    public Manager(String name) {
        super(name, XillaManager.getInstance());
        this.name = name;
        this.data = new ConcurrentHashMap<>();
        this.cacheList = new ConcurrentHashMap<>();

        if(!(this instanceof XillaManager)) {
            XillaManager.getInstance().put(this);
        }
    }

    public ManagerCache<T> getCache(String key) {
        if(!cacheList.containsKey(key)) {
            cacheList.put(key, new ManagerCache<T>());
        }
        return cacheList.get(key);
    }

    public Manager(String name, String file) {
        this(name);
        this.config = new Config(file);
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

    protected abstract void load();

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
}
