package net.xilla.core.library.config.extension;

import lombok.Getter;
import net.xilla.core.library.config.extension.type.JsonFile;
import net.xilla.core.library.manager.Manager;

public class ExtensionManager extends Manager<ConfigExtension> {

    @Getter
    private static ExtensionManager instance = new ExtensionManager();

    public ExtensionManager() {
        super("ConfigExtension");

        put(new ConfigExtension("json", new JsonFile(), "json"));
        put(new ConfigExtension("rjson", new JsonFile(), "json"));
    }

    @Override
    protected void objectAdded(ConfigExtension obj) {

    }

    @Override
    protected void objectRemoved(ConfigExtension obj) {

    }

}
