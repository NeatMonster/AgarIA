package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketAddCell extends ServerPacket {
    public int id;

    @Override
    public void read(final ByteBuffer buf) {
        id = buf.getInt();
    }
}
