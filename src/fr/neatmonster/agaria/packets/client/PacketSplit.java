package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketSplit extends ClientPacket {

    public PacketSplit() {
        super((byte) (17 & 0xff));
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