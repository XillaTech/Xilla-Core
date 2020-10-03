package net.xilla.core.log;

import lombok.RequiredArgsConstructor;

/**
 * Used to define the log level of the core.
 */
@RequiredArgsConstructor
public enum LogLevel {

    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3),
    FATAL(4);

    public final int level;

}
