package net.xilla.core.library.net.base;

import net.xilla.core.library.net.ResponseType;

public interface SendingExecutor {

    boolean run(ResponseType type, String address, String data);

}
