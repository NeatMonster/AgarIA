package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import fr.neatmonster.agaria.packets.ServerPacket;
import fr.neatmonster.agaria.utils.Pair;

public class PacketUpdateCells extends ServerPacket {
    public static final class Update {
        public int    id;
        public int    x;
        public int    y;
        public short  size;
        public byte   r;
        public byte   g;
        public byte   b;
        public byte   flags;
        public String skin;
        public String name;
    }

    public final List<Pair<Integer, Integer>> eats     = new ArrayList<>();
    public final List<Update>                 updates  = new ArrayList<>();
    public final List<Integer>                removals = new ArrayList<>();

    @Override
    public void read(final ByteBuffer buf) {
        final int eatsCnt = buf.getShort();
        for (int i = 0; i < (eatsCnt & 0xffff); ++i) {
            final Pair<Integer, Integer> eat = new Pair<>();
            eat.fst = buf.getInt();
            eat.snd = buf.getInt();
            eats.add(eat);
        }

        while (true) {
            final int id = buf.getInt();
            if (id == 0)
                break;
            final Update update = new Update();
            update.id = id;

            update.x = buf.getInt();
            update.y = buf.getInt();
            update.size = buf.getShort();

            update.r = buf.get();
            update.g = buf.get();
            update.b = buf.get();

            update.flags = buf.get();

            if ((update.flags & 2) > 0)
                buf.position(buf.position() + (buf.getInt() & 0xffffffff));

            update.skin = null;
            if ((update.flags & 4) > 0)
                while (true) {
                    final byte chr = buf.get();
                    if (chr == 0)
                        break;
                    if (update.skin == null)
                        update.skin = "";
                    update.skin += (char) (chr & 0xff);
                }

            update.name = null;
            while (true) {
                final short chr = buf.getShort();
                if (chr == 0)
                    break;
                if (update.name == null)
                    update.name = "";
                update.name += (char) (chr & 0xffff);
            }

            updates.add(update);
        }

        final int removalsCnt = buf.getInt();
        for (int i = 0; i < (removalsCnt & 0xffffffff); ++i)
            removals.add(buf.getInt());
    }
}
