package net.xilla.core.library.config;

import lombok.Getter;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.Manager;
import net.xilla.core.library.manager.ManagerObject;

public class Settings extends ManagerObject {

    public Settings(String file) {
        super(file, new Manager("Settings-" + file, file, Settings.class));
        setExtension(getManager().getConfig().getConfigFile().getExtension());
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
                getManager().getConfig().set(key.toString(), value);
            }
        }

        getManager().getConfig().save();
    }

    public void load() {
        loadSerializedData(getManager().getConfig().getJson());
    }

}
