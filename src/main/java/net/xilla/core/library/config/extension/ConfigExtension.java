package net.xilla.core.library.config.extension;

import lombok.Getter;
import net.xilla.core.library.config.ConfigFile;
import net.xilla.core.library.manager.ManagerObject;

public class ConfigExtension extends ManagerObject {

    @Getter
    private ConfigFile configFile;

    @Getter
    private String rawExtension;

    public ConfigExtension(String extension, ConfigFile configFile, String rawExtension) {
        super(extension.toLowerCase(), "ConfigExtension");

        this.rawExtension = rawExtension;
        this.configFile = configFile;
    }

}
