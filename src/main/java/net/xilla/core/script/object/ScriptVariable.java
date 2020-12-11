package net.xilla.core.script.object;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.library.XillaLibrary;
import net.xilla.core.library.json.SerializedObject;
import net.xilla.core.library.json.XillaJson;

public class ScriptVariable<T> implements XillaLibrary, SerializedObject {

    private Object variable;

    @Setter
    @Getter
    private String label;

    @Setter
    @Getter
    private String description;

    public ScriptVariable(Object variable) {
        this.variable = variable;
    }

    public T get() {
        return (T)variable;
    }

    public void set(Object variable) {
        this.variable = variable;
    }

    @Override
    public XillaJson getSerializedData() {
        return new XillaJson().put("data", variable)
                .put("label", label)
                .put("description", description);
    }

    @Override
    public void loadSerializedData(XillaJson json) {
        this.variable = json.get("data");
        this.label = json.get("label");
        this.description = json.get("description");
    }

}
