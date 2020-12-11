package net.xilla.core.script.function;

import net.xilla.core.log.Logger;
import net.xilla.core.script.ScriptException;
import net.xilla.core.script.ScriptLine;
import net.xilla.core.script.ScriptObject;

import java.util.ArrayList;
import java.util.List;

public class FunctionParser {

    private List<ScriptFunction> functions = new ArrayList<>();
    private ScriptLine[] lines;
    private String label;

    public FunctionParser(String label, ScriptLine... lines) {
        this.lines = lines;
        this.label = label;

        ScriptFunction print = new ScriptFunction("print");
        print.addExecutor((line, variables) -> {
            String data = variables[0].get().toString();
            if(data.startsWith("\"") && data.endsWith("\"")) {
            } else {
                Logger.log(new ScriptException("There is no valid data inside the print statement on line " + line), this.getClass());
            }
            return null;
        });

        addFunction(print);
    }

    public void addFunction(ScriptFunction function) {
        functions.add(function);
    }

    public ScriptObject[] run() throws Exception {
        ScriptObject[] objects = new ScriptObject[lines.length];
        for(int i = 0; i < lines.length; i++) {
            ScriptFunction[] f = new ScriptFunction[functions.size()];

            for(int j = 0; j < f.length; j++) {
                f[j] = functions.get(j);
            }

            objects[i] = new ScriptObject(f);
        }
        return objects;
    }

    public ScriptFunction build() {
        ScriptFunction function = new ScriptFunction(label);
        for(ScriptLine scriptLine : lines) {
//            ScriptExecutor executor = (line, variables) -> {
                ScriptFunction[] f = new ScriptFunction[functions.size()];

                for(int i = 0; i < f.length; i++) {
                    f[i] = functions.get(i);
                }
//
//                return new ScriptObject(f);
//            };
//            function.addExecutor(executor);

            function.addExecutor(scriptLine.getExecutor(f));
        }
        return function;
    }

}
