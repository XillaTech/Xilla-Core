package net.xilla.core.script.function;

import lombok.Getter;
import lombok.Setter;
import net.xilla.core.script.object.ScriptVariable;

public class ScriptFunction {

    @Getter
    private String label;
    @Getter
    private ScriptVariable[] variables;
    @Getter
    @Setter
    private ScriptExecutor executor;

    public ScriptFunction(String label, ScriptExecutor executor, ScriptVariable... variables) {
        this.label = label;
        this.executor = executor;
        this.variables = variables;
    }

    public void run() {
        executor.run(variables);
    }

    public void run(ScriptVariable... variables) {
        executor.run(variables);
    }

}
