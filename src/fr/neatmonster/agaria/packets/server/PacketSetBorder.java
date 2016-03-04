package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketSetBorder extends ServerPacket {
    public double minX;
    public double minY;
    public double maxX;
    public double maxY;

    @Override
    public void read(final ByteBuffer buf) {
        minX = buf.getDouble();
        minY = buf.getDouble();
        maxX = buf.getDouble();
        maxY = buf.getDouble();
    }
}
