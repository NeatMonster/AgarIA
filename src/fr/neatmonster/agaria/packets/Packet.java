package fr.neatmonster.agaria.packets;

public abstract class Packet {
    protected final byte packetId;

    protected Packet(final byte packetId) {
        this.packetId = packetId;
    }

    public final byte getPacketId() {
        return packetId;
    }
}
