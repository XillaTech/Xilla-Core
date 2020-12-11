package net.xilla.core.library.config.extension.type;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.config.ConfigFile;
import net.xilla.core.library.config.ConfigSection;
import net.xilla.core.library.json.XillaJson;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RegionalJsonFile extends XillaJson implements ConfigFile {

    @Getter
    @Setter
    private String file = null;

    public RegionalJsonFile() {

    }

    @Override
    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(formatJSONStr());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> T get(String key) {
        return null;
    }

    @Override
    public void set(String key, Object object) {

    }

    @Override
    public void reload() {
        File fileObject = new File(file);
        if(!fileObject.exists()) {
            try {
                if (fileObject.getParentFile() != null) {
                    fileObject.getParentFile().mkdirs();
                }
                fileObject.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileReader fileReader = new FileReader(file);
            parse(fileReader);
            fileReader.close();
        } catch (Exception e) {
        }
    }

    @Override
    public void clear() {
        setJson(new JSONObject());
        save();
    }

    @Override
    public boolean contains(String key) {
        return containsKey(key);
    }

    @Override
    public ConfigSection getSection(String key) {
        return new ConfigSection(key, new XillaJson(get(key)));
    }

    @Override
    public String getExtension() {
        return "json";
    }

    @Override
    public ConfigFile create(String file) {
       RegionalJsonFile configFile = duplicate();
       configFile.setFile(file);
       return configFile;
    }

    @Override
    public XillaJson getIndex() {
        return this;
    }

    private String formatJSONStr() {
        final String json_str = toJSONString();
        final int indent_width = 1;
        final char[] chars = json_str.toCharArray();
        final String newline = System.lineSeparator();

        String ret = "";
        boolean begin_quotes = false;

        for (int i = 0, indent = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == '\"') {
                ret += c;
                begin_quotes = !begin_quotes;
                continue;
            }

            if (!begin_quotes) {
                switch (c) {
                    case '{':
                    case '[':
                        ret += c + newline + String.format("%" + (indent += indent_width) + "s", "");
                        continue;
                    case '}':
                    case ']':
                        ret += newline + ((indent -= indent_width) > 0 ? String.format("%" + indent + "s", "") : "") + c;
                        continue;
                    case ':':
                        ret += c + " ";
                        continue;
                    case ',':
                        ret += c + newline + (indent > 0 ? String.format("%" + indent + "s", "") : "");
                        continue;
                    default:
                        if (Character.isWhitespace(c)) continue;
                }
            }

            ret += c + (c == '\\' ? "" + chars[++i] : "");
        }

        return ret;
    }

    public RegionalJsonFile duplicate() {
        RegionalJsonFile jsonFile = new RegionalJsonFile();
        jsonFile.setFile(file);
        jsonFile.setJson(getJson());
        return jsonFile;
    }

}
