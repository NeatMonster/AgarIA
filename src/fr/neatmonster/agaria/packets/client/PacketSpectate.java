package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketSpectate extends ClientPacket {

    public PacketSpectate() {
        super((byte) (1 & 0xff));
    }

    @Override
    public int getLength() {
        return 0;
    }

    // @formatter:off
    @Override
    public void write(final ByteBuffer buf) {}
    // @formatter:on
}