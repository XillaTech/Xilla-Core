package net.xilla.core.script.function;

import net.xilla.core.script.object.ScriptVariable;

public interface ScriptExecutor {

    Object run(ScriptVariable... variables);

}
