package net.xilla.core.library.config.extension;

import lombok.Getter;
import net.xilla.core.library.config.ConfigFile;
import net.xilla.core.library.manager.ManagerObject;

public class ConfigExtension extends ManagerObject {

    @Getter
    private ConfigFile configFile;


    public ConfigExtension(String extension, ConfigFile configFile) {
        super(extension.toLowerCase(), "ConfigExtension");

        this.configFile = configFile;
    }

}
