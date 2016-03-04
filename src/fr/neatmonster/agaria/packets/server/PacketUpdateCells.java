package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketUpdateCells extends ServerPacket {
    public static final class CellEat {
        public int eaterId;
        public int eatenId;
    }

    public static final class CellUpdate {
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

    public final List<CellEat>    eats     = new ArrayList<>();
    public final List<CellUpdate> updates  = new ArrayList<>();
    public final List<Integer>    removals = new ArrayList<>();

    public PacketUpdateCells() {
        super((byte) (16 & 0xff));
    }

    @Override
    public void read(final ByteBuffer buf) {
        final int eatsCnt = buf.getShort();
        for (int i = 0; i < (eatsCnt & 0xffff); ++i) {
            final CellEat eat = new CellEat();
            eat.eaterId = buf.getInt();
            eat.eatenId = buf.getInt();
            eats.add(eat);
        }

        while (true) {
            final int id = buf.getInt();
            if (id == 0)
                break;
            final CellUpdate update = new CellUpdate();
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
