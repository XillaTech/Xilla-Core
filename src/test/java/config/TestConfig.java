package config;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.config.Config;
import net.xilla.core.library.config.Settings;
import net.xilla.core.library.manager.StoredData;

public class Test extends Settings {

    public static void main(String[] args) {
        new Test();
    }

    @StoredData
    @Getter
    @Setter
    public String setting = "value";

    public Test() {
        super("/test-file.json");
        startup();
    }

}
