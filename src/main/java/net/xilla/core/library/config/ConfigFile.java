package net.xilla.core.library.config;

import net.xilla.core.library.json.XillaJson;

public interface ConfigFile {

    // CONTROLS

    void save();

    void reload();

    void clear();

    // GETTERS AND SETTERS

    Object get(String key);

    void set(String key, Object object);

    void remove(String key);

    // INTERNAL CALLS

    ConfigFile create(String file);

    XillaJson getIndex();

    // EXTERNAL CALLS

    boolean contains(String key);

    ConfigSection getSection(String key);

    String getExtension();

    // FOR ADVANCED CONFIGS

    default boolean load(String key) { return true; }

    default boolean unload(String key) { return true; }

}
