package net.xilla.core.script.function;

import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;
import net.xilla.core.script.ScriptException;
import net.xilla.core.script.ScriptLine;
import net.xilla.core.script.object.ScriptVariable;

import java.util.ArrayList;
import java.util.List;

public class FunctionParser extends ScriptLine {

    private List<ScriptFunction> functions = new ArrayList<>();

    public void addFunction(ScriptFunction function) {
        functions.add(function);
    }

    public FunctionParser(int lineNumber, String line) {
        super(lineNumber, line);

        addFunction(new ScriptFunction("print", (variables) -> {
            String data = variables[0].get().toString();
            if(data.startsWith("\"") && data.endsWith("\"")) {
                System.out.println(data.substring(1, data.length() - 1));
            } else {
                Logger.log(new ScriptException("There is no valid data inside the print statement on line " + getLineNumber()), this.getClass());
            }
            return null;
        }));
    }

    @Override
    public void run() {
        for(ScriptFunction function : functions) {
            if (getLine().startsWith(function.getLabel() + "(\"") && getLine().endsWith("\")")) {
                String data = getLine().substring(6, getLine().length() - 1);
                function.run(new ScriptVariable(data));
            } else if (getLine().startsWith(function.getLabel() + "(") && getLine().endsWith(")")) {
                String data = getLine().substring(6, getLine().length() - 1);
                function.run(new ScriptVariable(data));
            }
        }
    }

}
