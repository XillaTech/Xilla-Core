package net.xilla.core.script;

import lombok.Getter;
import net.xilla.core.script.function.ScriptExecutor;
import net.xilla.core.script.function.ScriptFunction;
import net.xilla.core.script.object.ScriptVariable;

public class ScriptLine {

    @Getter
    private String label;
    @Getter
    private int lineNumber;
    @Getter
    private String line;

    public ScriptLine(int lineNumber, String line, String label) {
        this.lineNumber = lineNumber;
        this.line = line;
        this.label = label;
    }

    public ScriptObject[] run(ScriptFunction... functions) throws Exception {
        ScriptObject[] objects = new ScriptObject[functions.length];
        for(int i = 0; i < functions.length; i++) {
            if (getLine().startsWith(getLabel() + "(\"") && getLine().endsWith("\")")) {
                String data = getLine().substring(6, getLine().length() - 1);
                objects[i] = new ScriptObject(functions[i].run(lineNumber, new ScriptVariable(data)));
            } else if (getLine().startsWith(functions[i].getLabel() + "(") && getLine().endsWith(")")) {
                String data = getLine().substring(6, getLine().length() - 1);
                objects[i] = new ScriptObject(functions[i].run(lineNumber, new ScriptVariable(data)));
            }
        }
        return objects;
    }

    public ScriptExecutor getExecutor(ScriptFunction... functions) {
        return (line, variables) -> {
            ScriptObject[] objects = new ScriptObject[functions.length];
            for(int i = 0; i < functions.length; i++) {
                if (getLine().startsWith(getLabel() + "(\"") && getLine().endsWith("\")")) {
                    String data = getLine().substring(6, getLine().length() - 1);
                    objects[i] = new ScriptObject(functions[i].run(lineNumber, new ScriptVariable(data)));
                } else if (getLine().startsWith(functions[i].getLabel() + "(") && getLine().endsWith(")")) {
                    String data = getLine().substring(6, getLine().length() - 1);
                    objects[i] = new ScriptObject(functions[i].run(lineNumber, new ScriptVariable(data)));
                }
            }
            return new ScriptObject(objects);
        };
    }

}
