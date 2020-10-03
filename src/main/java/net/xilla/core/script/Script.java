package net.xilla.core.script;

import net.xilla.core.log.Logger;
import net.xilla.core.script.function.FunctionParser;
import net.xilla.core.script.object.ScriptVariable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class Script {

    private List<ScriptLine> code;

    public Script() {
        this.code = new Vector<>();
    }


    public Script(String file) {
        this.code = new Vector<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            int i = 0;
            while (line != null) {
                code.add(new FunctionParser(i, line));
                line = reader.readLine();
                i++;
            }
            reader.close();
        } catch (IOException e) {
            Logger.log(e, this.getClass());
        }
    }

    public void addLine(ScriptLine line) {
        code.add(line);
    }

    public void run() {
        for(ScriptLine line : code) {
            line.run();
        }
    }

}
