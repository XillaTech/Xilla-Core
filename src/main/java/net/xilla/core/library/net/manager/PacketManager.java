package net.xilla.core.library.net.manager;

import net.xilla.core.library.manager.Manager;

public class PacketManager extends Manager<Packet> {

    private static PacketManager instance = new PacketManager();

    public static PacketManager getInstance() {
        return instance;
    }

    public PacketManager() {
        super("PacketManager");
    }

    @Override
    protected void load() {

    }

    @Override
    protected void objectAdded(Packet obj) {

    }

    @Override
    protected void objectRemoved(Packet obj) {

    }

}
