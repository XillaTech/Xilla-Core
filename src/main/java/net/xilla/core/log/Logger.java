package net.xilla.core.log;

import lombok.Getter;
import lombok.Setter;

import java.util.TimeZone;

public class Logger {

    @Getter
    @Setter
    private static LogLevel logLevel = LogLevel.INFO;

    @Getter
    @Setter
    private static Log logger = new CoreLogger();

    @Getter
    @Setter
    private static TimeZone timeZone = TimeZone.getTimeZone("UTC");

    public static void log(LogLevel level, String message, Class clazz) {
        logger.log(level, message, clazz);
    }

    public static void log(LogLevel level, Throwable message, Class clazz) {
        logger.log(level, message, clazz);
    }

    public static void log(Throwable message, Class clazz) {
        logger.log(message, clazz);
    }

}
