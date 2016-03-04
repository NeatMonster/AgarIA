package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketEjectMass extends ClientPacket {

    public PacketEjectMass() {
        super((byte) (21 & 0xff));
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