package net.xilla.core.library.config;

import net.xilla.core.library.json.XillaJson;

public interface ConfigFile {

    // CONTROLS

    void save();

    void reload();

    void clear();

    // GETTERS AND SETTERS

    <T> T get(String key);

    void set(String key, Object object);

    // INTERNAL CALLS

    ConfigFile create(String file);

    XillaJson getIndex();

    // EXTERNAL CALLS

    boolean contains(String key);

    ConfigSection getSection(String key);

}
