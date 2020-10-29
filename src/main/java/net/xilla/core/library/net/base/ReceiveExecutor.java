package net.xilla.core.library.net.base;

import net.xilla.core.library.net.ResponseType;

public interface ReceiveExecutor {

    String run(ResponseType type, String address, String data);

}