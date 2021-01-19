package net.xilla.core.library.config;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.manager.Manager;

public class ConfigManager extends Manager<String, Config> {

    @Getter
    private static ConfigManager instance = new ConfigManager();

    @Getter
    @Setter
    private String baseFolder = "";

    public ConfigManager() {
        super("Configs");
    }

}
