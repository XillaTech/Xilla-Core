package net.xilla.core.reflection;

import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.Manager;
import net.xilla.core.library.manager.ManagerObject;

import java.lang.reflect.Field;

public abstract class Reflection<T> extends ManagerObject {

    public Reflection(Class<T> clazz) {
        super(clazz, "Reflection");
    }

    public abstract T loadFromSerializedData(Object base, Field field, Object object);

    public abstract Object getSerializedData(Object base, Field field, T object);

}
