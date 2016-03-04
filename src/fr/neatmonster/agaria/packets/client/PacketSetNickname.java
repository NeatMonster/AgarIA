package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketSetNickname extends ClientPacket {
    public String name;

    public PacketSetNickname() {
        super((byte) (0 & 0xff));
    }

    @Override
    public int getLength() {
        return 2 * name.toCharArray().length;
    }

    @Override
    public void write(final ByteBuffer buf) {
        final char[] chars = name.toCharArray();
        final ByteBuffer charsBuf = ByteBuffer.allocate(2 * chars.length);
        charsBuf.order(ByteOrder.LITTLE_ENDIAN);
        for (final char c : chars)
            charsBuf.putChar(c);
        charsBuf.rewind();
        buf.put(charsBuf);
    }
}
