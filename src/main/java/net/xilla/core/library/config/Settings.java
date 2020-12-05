package net.xilla.spigotcore.tool;

import lombok.Getter;
import net.xilla.core.library.config.Config;
import net.xilla.core.library.config.ConfigManager;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import net.xilla.spigotcore.manager.MCObject;

public class Settings extends MCObject {

    @Getter
    private Config config;

    public Settings(String file) {
        this.config = new Config(file);
        ConfigManager.getInstance().put(config);
    }

    public void startup() {
        load();
        save();
    }

    public void save() {
        XillaJson json = getSerializedData();

        for(Object key : json.getJson().keySet()) {
            Object value = json.getJson().get(key);

            if(value != null) {
                this.config.set(key.toString(), value);
            }
        }

        this.config.save();
    }

    public void load() {
        loadSerializedData(config.getJson());
    }

}
