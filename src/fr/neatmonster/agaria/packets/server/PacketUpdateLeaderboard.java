package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import fr.neatmonster.agaria.packets.ServerPacket;
import fr.neatmonster.agaria.utils.Pair;

public class PacketUpdateLeaderboard extends ServerPacket {
    public final List<Pair<Integer, String>> leaderboard = new ArrayList<>();

    @Override
    public void read(final ByteBuffer buf) {
        final int entriesCnt = buf.getInt();
        for (int i = 0; i < (entriesCnt & 0xffffffff); ++i) {
            final Pair<Integer, String> entry = new Pair<>();
            entry.fst = buf.getInt();
            entry.snd = null;
            while (true) {
                final short chr = buf.getShort();
                if (chr == 0)
                    break;
                if (entry.snd == null)
                    entry.snd = "";
                entry.snd += (char) (chr & 0xffff);
            }
            leaderboard.add(entry);
        }
    }
}
