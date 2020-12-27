package net.xilla.core.library.config;

import lombok.Getter;
import net.xilla.core.library.config.extension.ConfigExtension;
import net.xilla.core.library.config.extension.ExtensionManager;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public class Config extends ManagerObject {

    private String file;

    @Getter
    private ConfigFile configFile;

    public Config(String file) {
        super(file, ConfigManager.getInstance());

        this.file = file;

        load();
        reload();
    }

    public Config() {
    }

    private void load() {
        this.file = ConfigManager.getInstance().getBaseFolder() + file;

        String[] temp = file.split("\\.");
        String ext = temp[temp.length - 1];
        ConfigExtension extension = ExtensionManager.getInstance().get(ext.toLowerCase());
        if(extension == null) {
            Logger.log(LogLevel.ERROR, "Invalid config extension type for config " + file, getClass());
            Logger.log(LogLevel.ERROR, "Defaulting to JSON!", getClass());
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < temp.length - 1; i++) {
                builder.append(temp[i]).append(".");
            }
            builder.append("json");

            this.file = builder.toString();
            setKey(file);
            extension = ExtensionManager.getInstance().get("json");
        }

        if(extension == null) {
            Logger.log(LogLevel.FATAL, "FAILED TO LOAD JSON CONFIG EXTENSION, SOMETHING HAS GONE TERRIBLY WRONG!", getClass());
            return;
        }
        this.configFile = extension.getConfigFile().create(file);
    }

    public void reload() {
        configFile.reload();
    }

    public void save() {
        configFile.save();
    }

    public boolean setDefault(String key, Object value) {
        if(!configFile.contains(key)) {
            configFile.set(key, value);
            return true;
        }
        return false;
    }

    public void set(String key, Object value) {
        configFile.set(key, value);
    }

    public XillaJson getJson() {
        XillaJson json = configFile.getIndex();

        json.put("file-extension", configFile.getExtension());

        return json;
    }

    public <T> T get(String key) {
        return (T)configFile.get(key);
    }

    public String getString(String key) {
        return (String)configFile.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(configFile.get(key).toString());
    }

    public double getDouble(String key) {
        return Double.parseDouble(configFile.get(key).toString());
    }

    public float getFloat(String key) {
        return Float.parseFloat(configFile.get(key).toString());
    }

    public long getLong(String key) {
        return Long.parseLong(configFile.get(key).toString());
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(configFile.get(key).toString());
    }

    public JSONObject getJSON(String key) {
        return (JSONObject)configFile.get(key);
    }

    public Map<String, String> getMap(String key) {
        return (Map<String, String>)configFile.get(key);
    }

    public List getList(String key) {
        return (List)configFile.get(key);
    }

    public void clear() {
        configFile.clear();
    }

    @Override
    public XillaJson getSerializedData() {
        XillaJson json = new XillaJson();

        json.put("file", file);
        json.put("index", configFile.getIndex());
        json.put("file-extension", configFile.getExtension());

        return json;
    }

    public boolean load(String key) {
        return configFile.load(key);
    }

    public boolean unload(String key) {
        return configFile.unload(key);
    }

    @Override
    public void loadSerializedData(XillaJson json) {
        this.file = json.get("file");
        load();
    }

}
