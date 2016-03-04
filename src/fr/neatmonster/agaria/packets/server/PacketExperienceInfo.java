package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketExperienceInfo extends ServerPacket {
    public int level;
    public int curExp;
    public int nextExp;

    public PacketExperienceInfo() {
        super((byte) (81 & 0xff));
    }

    @Override
    public void read(final ByteBuffer buf) {
        level = buf.getInt();
        curExp = buf.getInt();
        nextExp = buf.getInt();
    }
}
