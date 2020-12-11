package net.xilla.core.library.net.manager.packet;

import net.xilla.core.library.manager.Manager;

public class PacketManager extends Manager<String, Packet> {

    private static PacketManager instance = new PacketManager();

    public static PacketManager getInstance() {
        return instance;
    }

    public PacketManager() {
        super("PacketManager", Packet.class);
    }

}
