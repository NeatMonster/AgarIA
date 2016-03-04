package fr.neatmonster.agaria.packets;

import java.nio.ByteBuffer;

public abstract class ServerPacket extends Packet {

    protected ServerPacket(final byte packetId) {
        super(packetId);
    }

    public abstract void read(final ByteBuffer buf);
}
