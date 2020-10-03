package net.xilla.core.script.object;

public class ScriptInteger extends ScriptVariable<Integer> {

    public ScriptInteger(Integer variable) {
        super(variable);
    }

    public ScriptInteger(String variable) {
        super(Integer.parseInt(variable));
    }

}
