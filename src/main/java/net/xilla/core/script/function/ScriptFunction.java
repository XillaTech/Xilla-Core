package net.xilla.core.script.function;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.script.ScriptObject;
import net.xilla.core.script.object.ScriptVariable;

import java.util.ArrayList;

public class ScriptFunction {

    @Getter
    private String label;
    @Getter
    private ScriptVariable[] variables;
    @Getter
    @Setter
    private ArrayList<ScriptExecutor> executors;

    public ScriptFunction(String label, ScriptVariable... variables) {
        this.label = label;
        this.executors = new ArrayList<>();
        this.variables = variables;
    }

    public void addExecutor(ScriptExecutor executor) {
        executors.add(executor);
    }

    public ScriptObject[] run(int line, ScriptVariable... variables) throws Exception {
        ScriptObject[] objects = new ScriptObject[executors.size()];
        for(int i = 0; i < executors.size(); i++) {
            objects[i] = executors.get(i).run(line, variables);
        }
        return objects;
    }

}
