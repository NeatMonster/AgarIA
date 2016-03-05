package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketUpdateTeamsScores extends ServerPacket {
    public final List<Float> scores = new ArrayList<>();

    @Override
    public void read(final ByteBuffer buf) {
        final int scoresCount = buf.getInt();
        for (int i = 0; i < (scoresCount & 0xffffffff); ++i)
            scores.add(buf.getFloat());
    }
}
