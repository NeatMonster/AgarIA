package fr.neatmonster.agaria.packets;

import java.nio.ByteBuffer;

public abstract class ClientPacket extends Packet {
    private final byte packetId;

    protected ClientPacket(final byte packetId) {
        this.packetId = packetId;
    }

    public final byte getPacketId() {
        return packetId;
    }

    public int getLength() {
        return 0;
    }

    public void write(final ByteBuffer buf) {}
}
