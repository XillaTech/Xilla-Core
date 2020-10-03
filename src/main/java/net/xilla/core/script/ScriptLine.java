package net.xilla.core.script;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class ScriptLine {

    @Getter
    private int lineNumber;
    @Getter
    private String line;

    public ScriptLine(int lineNumber, String line) {
        this.lineNumber = lineNumber;
        this.line = line;
    }

    public abstract void run();

}
