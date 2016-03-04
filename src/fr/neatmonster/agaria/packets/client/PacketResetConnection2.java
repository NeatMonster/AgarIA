package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketResetConnection2 extends ClientPacket {

    public PacketResetConnection2() {
        super((byte) (255 & 0xff));
    }

    @Override
    public int getLength() {
        return 4;
    }

    @Override
    public void write(final ByteBuffer buf) {
        buf.putInt(0x33182283);
    }
}
