package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketDrawLine extends ServerPacket {
    public short x;
    public short y;

    @Override
    public void read(final ByteBuffer buf) {
        x = buf.getShort();
        y = buf.getShort();
    }
}
