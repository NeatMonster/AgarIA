package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketUpdateView extends ServerPacket {
    public float x;
    public float y;
    public float zoom;

    @Override
    public void read(final ByteBuffer buf) {
        x = buf.getFloat();
        y = buf.getFloat();
        zoom = buf.getFloat();
    }
}
