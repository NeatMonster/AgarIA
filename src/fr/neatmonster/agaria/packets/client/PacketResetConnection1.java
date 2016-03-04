package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketResetConnection1 extends ClientPacket {

    public PacketResetConnection1() {
        super((byte) (254 & 0xff));
    }

    @Override
    public int getLength() {
        return 4;
    }

    @Override
    public void write(final ByteBuffer buf) {
        buf.putInt(0x05000000);
    }
}
