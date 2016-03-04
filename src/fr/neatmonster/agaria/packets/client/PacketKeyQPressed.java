package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketKeyQPressed extends ClientPacket {

    public PacketKeyQPressed() {
        super((byte) (18 & 0xff));
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
