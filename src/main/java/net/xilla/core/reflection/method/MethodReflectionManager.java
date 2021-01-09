package net.xilla.core.reflection.method;

import lombok.Getter;
import net.xilla.core.library.config.ConfigFile;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.Manager;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import net.xilla.core.reflection.storage.StorageReflection;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

public class MethodReflectionManager extends Manager<String, MethodReflection> {

    @Getter
    private static MethodReflectionManager instance = new MethodReflectionManager();

    public MethodReflectionManager() {
        super("MethodReflection", MethodReflection.class);

        instance = this;
    }

}
