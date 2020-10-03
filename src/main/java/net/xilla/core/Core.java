package net.xilla.core;

import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

public class Core {

    public static void main(String[] args) {
        Logger.log(LogLevel.INFO, "This is an example log", Core.class);
        Logger.log(new Exception("This is an example error"), Core.class);
    }

}
