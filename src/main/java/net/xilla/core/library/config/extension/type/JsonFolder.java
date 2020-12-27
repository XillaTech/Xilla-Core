package net.xilla.core.library.config.extension.type;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.config.ConfigFile;
import net.xilla.core.library.config.ConfigSection;
import net.xilla.core.library.json.XillaJson;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.tools.FileObject;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonFolder implements ConfigFile {

    @Getter
    @Setter
    private String folder = null;

    @Getter
    @Setter
    private Map<String, XillaJson> data = new ConcurrentHashMap<>();

    @Override
    public void save() {
        data.keySet().parallelStream().forEach((key) -> saveElement(key, data.get(key)));
    }

    public void saveElement(String key, XillaJson element) {
        try {
            File file = new File(folder + "/" + key + ".json");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(formatJSONStr(element));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public void set(String key, Object object) {
        data.put(key, new XillaJson((JSONObject) object));
    }

    @Override
    public void remove(String key) {
        File file = new File(folder + "/" + key + ".json");
        file.delete();
    }

    @Override
    public void reload() {
        File fol = new File(folder);
        if(!fol.exists()) {
            fol.mkdirs();
        }
        data.keySet().parallelStream().forEach((key) -> {
            File file = new File(folder + "/" + key + ".json");
            try {
                FileReader fileReader = new FileReader(file);
                XillaJson json = new XillaJson().parse(fileReader);
                fileReader.close();
            } catch (Exception e) {
            }
        });
    }

    @Override
    public void clear() {
        data = new ConcurrentHashMap<>();

        File fol = new File(folder);
        if(fol.listFiles() != null) {
            Arrays.stream(fol.listFiles()).forEach((file -> {
                file.delete();
            }));
        }
    }

    @Override
    public boolean contains(String key) {
        if(data.containsKey(key)) {
            return true;
        }

        File file = new File(folder + "/" + key + ".json");
        if(file.exists()) {
            return true;
        }

        return false;
    }

    @Override
    public ConfigSection getSection(String key) {
        File file = new File(folder + "/" + key + ".json");

        try {
            FileReader reader = new FileReader(file);
            ConfigSection section = new ConfigSection(key, new XillaJson().parse(reader));
            reader.close();
            return section;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getExtension() {
        return "jsonf";
    }

    @Override
    public ConfigFile create(String f) {
       JsonFolder configFile = new JsonFolder();
       configFile.setFolder(f.replace(".jsonf", ""));
       return configFile;
    }

    @Override
    public XillaJson getIndex() {
        XillaJson json = new XillaJson();
        File fol = new File(folder);
        if(fol.listFiles() != null) {
            Arrays.stream(fol.listFiles()).forEach((file -> {
                if(file.isFile()) {
                    String key = file.getName().replace(".json", "");
                    json.put(key, data.containsKey(key));
                }
            }));
        }
        return json;
    }

    private String formatJSONStr(XillaJson json) {
        final String json_str = json.toJSONString();
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

    public JsonFolder duplicate() {
        JsonFolder jsonFile = new JsonFolder();
        jsonFile.setFolder(folder.replace(".jsonf", ""));
        jsonFile.setData(data);
        return jsonFile;
    }

    @Override
    public boolean load(String key) {
        if(contains(key)) {
            ConfigSection section = getSection(key);
            data.put(section.getKey(), section.getJson());
            return true;
        }
        return false;
    }

    @Override
    public boolean unload(String key) {
        if(contains(key)) {
            return data.remove(key) != null;
        }
        return false;
    }

}
