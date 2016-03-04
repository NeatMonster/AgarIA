package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketKeyQReleased extends ClientPacket {

    public PacketKeyQReleased() {
        super((byte) (19 & 0xff));
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
