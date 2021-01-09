package net.xilla.core.reflection.storage;

import net.xilla.core.library.config.ConfigFile;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.Manager;
import net.xilla.core.library.manager.ManagerObject;

import java.lang.reflect.Field;

public abstract class StorageReflection<T> extends ManagerObject {

    public StorageReflection(Class<T> clazz) {
        super(clazz, "StorageReflection");
    }

    public abstract T loadFromSerializedData(ConfigFile file, Object base, Field field, Object object);

    public abstract Object getSerializedData(ConfigFile file, Object base, Field field, T object);

}
