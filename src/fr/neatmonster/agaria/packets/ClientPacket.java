package fr.neatmonster.agaria.packets;

import java.nio.ByteBuffer;

public abstract class ClientPacket extends Packet {

    protected ClientPacket(final byte packetId) {
        super(packetId);
    }

    public abstract int getLength();

    public abstract void write(final ByteBuffer buf);
}
