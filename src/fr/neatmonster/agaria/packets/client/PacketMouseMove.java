package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketMouseMove extends ClientPacket {
    public int x;
    public int y;
    public int id;

    public PacketMouseMove() {
        super((byte) (16 & 0xff));
    }

    @Override
    public int getLength() {
        return 12;
    }

    @Override
    public void write(final ByteBuffer buf) {
        final ByteBuffer tmpBuf = ByteBuffer.allocate(12);
        tmpBuf.order(ByteOrder.LITTLE_ENDIAN);
        tmpBuf.putInt(x);
        tmpBuf.putInt(y);
        tmpBuf.putInt(id);
        tmpBuf.rewind();
        buf.put(tmpBuf);
    }
}