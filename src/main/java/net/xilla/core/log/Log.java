package net.xilla.core.log;

public interface Log {

    void log(LogLevel level, String message, Class clazz);

    void log(LogLevel level, Throwable message, Class clazz);

    void log(Throwable message, Class clazz);

}
