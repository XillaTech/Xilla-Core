package net.xilla.core.script;

import net.xilla.core.log.Logger;
import net.xilla.core.script.function.FunctionParser;
import net.xilla.core.script.function.ScriptFunction;
import net.xilla.core.script.object.ScriptVariable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Script {

    private List<ScriptFunction> code;

    public Script() {
        this.code = new Vector<>();
    }

    public Script(String file) {
        this.code = new Vector<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            ArrayList<String> data = new ArrayList<>();
            while (line != null) {
                data.add(line);
                line = reader.readLine();
            }
            reader.close();

            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < data.size(); i++) {
                builder.append(data.get(i));
                if(data.get(i).endsWith(";")) {
                    String temp = builder.toString().substring(0, builder.length() - 1);
                    code.add(new FunctionParser(file, new ScriptLine(i + 1, temp, file)).build());
                    builder = new StringBuilder();
                }
            }
        } catch (IOException e) {
            Logger.log(e, this.getClass());
        }
    }

    public void add(FunctionParser function) {
        code.add(function.build());
    }

    public void add(ScriptFunction function) {
        code.add(function);
    }

    public ScriptObject[] run() throws Exception {
        ScriptObject[] objects = new ScriptObject[code.size()];
        for(int i = 0; i < code.size(); i++) {
            code.get(i).run(i);
        }
        return objects;
    }

}
