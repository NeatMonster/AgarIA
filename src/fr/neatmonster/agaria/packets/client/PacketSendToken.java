package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketSendToken extends ClientPacket {
    public String token;

    public PacketSendToken() {
        super((byte) (80 & 0xff));
    }

    @Override
    public int getLength() {
        try {
            return token.getBytes().length;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void write(final ByteBuffer buf) {
        try {
            buf.put(token.getBytes("UTF-8"));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
