package net.xilla.core.library.config;

import lombok.Getter;
import net.xilla.core.library.json.XillaJson;

public class ConfigSection {

    @Getter
    private String key;

    @Getter
    private XillaJson json;

    public ConfigSection(String key, XillaJson json) {
        this.key = key;
        this.json = json;
    }

}
