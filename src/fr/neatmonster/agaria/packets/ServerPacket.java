package fr.neatmonster.agaria.packets;

import java.nio.ByteBuffer;

public abstract class ServerPacket extends Packet {

    public void read(final ByteBuffer buf) {}
}
