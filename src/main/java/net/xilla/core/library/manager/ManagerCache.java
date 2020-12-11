package net.xilla.core.library.manager;

import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ManagerCache<Key, Value extends ManagerObject>{

    @Getter
    private ConcurrentHashMap<Key, Value> cache = new ConcurrentHashMap<>();

    public void putObject(Key key, Value object) {
        cache.put(key, object);
    }

    public boolean isCached(Key key) {
        return cache.containsKey(key);
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(Key key) {
        return cache.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public Value get(Key key) {
        return cache.get(key);
    }

    public Value put(Key key, Value value) {
        return cache.put(key, value);
    }

    public Value remove(Key key) {
        return cache.remove(key);
    }

    public void putAll(Map<? extends Key, ? extends Value> m) {
        cache.putAll(m);
    }

    public void clear() {
        cache.clear();
    }

    public Set<Key> keySet() {
        return cache.keySet();
    }

    public Collection<Value> values() {
        return cache.values();
    }

}
