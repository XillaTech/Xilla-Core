package net.xilla.core.script.object;

public class ScriptDouble extends ScriptVariable<Double> {

    public ScriptDouble(Double variable) {
        super(variable);
    }

    public ScriptDouble(String variable) {
        super(Double.parseDouble(variable));
    }

}
