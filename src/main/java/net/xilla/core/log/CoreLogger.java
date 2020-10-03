package net.xilla.core.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CoreLogger implements Log {

    @Override
    public void log(LogLevel level, String message, Class clazz) {
        if(level.level >= Logger.getLogLevel().level) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            df.setTimeZone(Logger.getTimeZone());
            String nowAsISO = df.format(new Date());

            String log = "[" + nowAsISO + "] [" + level.name() + "] (" + clazz.getName() + ") " + message;
            System.out.println(log);
        }
    }

    @Override
    public void log(LogLevel level, Throwable message, Class clazz) {
        if(level.level >= Logger.getLogLevel().level) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            df.setTimeZone(Logger.getTimeZone());
            String nowAsISO = df.format(new Date());

            String log = "[" + nowAsISO + "] [" + level.name() + "] (" + clazz.getName() + ") " + message.toString();

            System.out.println(log);

            for(int i = 0; i < message.getStackTrace().length; i++) {
                log = "[" + nowAsISO + "] [" + level.name() + "] (" + clazz.getName() + ") [ERR] " + message.getStackTrace()[i].toString();
                System.out.println(log);
            }
        }
    }

    @Override
    public void log(Throwable message, Class clazz) {
        log(LogLevel.ERROR, message, clazz);
    }

}
