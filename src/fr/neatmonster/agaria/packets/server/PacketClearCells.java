package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketClearCells extends ServerPacket {

    public PacketClearCells() {
        super((byte) (18 & 0xff));
    }

    // @formatter:off
    @Override
    public void read(final ByteBuffer buf) {}
    // @formatter:on
}
