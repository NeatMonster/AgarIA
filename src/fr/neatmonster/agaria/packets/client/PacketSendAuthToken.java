package fr.neatmonster.agaria.packets.client;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketSendAuthToken extends ClientPacket {
    public byte   provider;
    public String token;

    public PacketSendAuthToken() {
        super((byte) (82 & 0xff));
    }

    @Override
    public int getLength() {
        try {
            return 1 + token.getBytes().length;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void write(final ByteBuffer buf) {
        buf.putInt(provider);
        try {
            buf.put(token.getBytes("UTF-8"));
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
