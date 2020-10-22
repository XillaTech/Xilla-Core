package net.xilla.core.script.function;

import net.xilla.core.script.ScriptObject;
import net.xilla.core.script.object.ScriptVariable;

public interface ScriptExecutor {

    ScriptObject run(int line, ScriptVariable... variables) throws Exception;

}
