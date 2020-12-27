package net.xilla.core.library.config.extension;

import lombok.Getter;
import net.xilla.core.library.config.extension.type.JsonFile;
import net.xilla.core.library.config.extension.type.JsonFolder;
import net.xilla.core.library.manager.Manager;

public class ExtensionManager extends Manager<String, ConfigExtension> {

    @Getter
    private static ExtensionManager instance = new ExtensionManager();

    public ExtensionManager() {
        super("ConfigExtension");

        put(new ConfigExtension("json", new JsonFile()));
        put(new ConfigExtension("jsonf", new JsonFolder()));
    }

    @Override
    protected void objectAdded(ConfigExtension obj) {

    }

    @Override
    protected void objectRemoved(ConfigExtension obj) {

    }

}
