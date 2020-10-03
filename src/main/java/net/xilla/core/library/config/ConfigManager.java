package net.xilla.core.library.config;

import lombok.Getter;
import net.xilla.core.library.manager.Manager;
import net.xilla.core.library.manager.XillaManager;

public class ConfigManager extends Manager<Config> {

    @Getter
    private static ConfigManager instance = new ConfigManager();

    public ConfigManager() {
        super("Configs");
    }

    @Override
    protected void load() {

    }

    @Override
    protected void objectAdded(Config obj) {

    }

    @Override
    protected void objectRemoved(Config obj) {

    }

}
