package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketUpdateLeaderboard extends ServerPacket {
    public static final class Entry {
        public int    id;
        public String name;
    }

    public final List<Entry> leaderboard = new ArrayList<>();

    public PacketUpdateLeaderboard() {
        super((byte) (49 & 0xff));
    }

    @Override
    public void read(final ByteBuffer buf) {
        final int entriesCnt = buf.getInt();
        for (int i = 0; i < (entriesCnt & 0xffffffff); ++i) {
            final Entry entry = new Entry();
            entry.id = buf.getInt();
            entry.name = null;
            while (true) {
                final short chr = buf.getShort();
                if (chr == 0)
                    break;
                if (entry.name == null)
                    entry.name = "";
                entry.name += (char) (chr & 0xffff);
            }
            leaderboard.add(entry);
        }
    }
}
