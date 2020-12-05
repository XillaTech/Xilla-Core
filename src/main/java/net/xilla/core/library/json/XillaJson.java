package net.xilla.core.library.json;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.XillaLibrary;
import net.xilla.core.log.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class XillaJson extends XillaLibrary {

    @Setter
    @Getter
    private JSONObject json;

    public XillaJson(JSONObject json) {
        this.json = json;
    }

    public XillaJson() {
        this(new JSONObject());
    }

    public <T> T get(String key) {
        if(key == null || !json.containsKey(key)) {
            return null;
        }
       return (T)json.get(key);
    }

    public XillaJson put(String key, Object object) {
       json.put(key, object);
       return this;
    }

    public boolean containsKey(String key) {
        return json.containsKey(key);
    }

    public String toJSONString() {
        return json.toJSONString();
    }

    public XillaJson parse(String text) {
        try {
            json = (JSONObject)new JSONParser().parse(text);
        } catch (ParseException e) {
            Logger.log(e, this.getClass());
        }
        return this;
    }

    public XillaJson parse(FileReader reader) throws IOException, ParseException {
        json = (JSONObject)new JSONParser().parse(reader);
        return this;
    }

}
