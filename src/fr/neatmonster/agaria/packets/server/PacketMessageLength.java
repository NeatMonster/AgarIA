package fr.neatmonster.agaria.packets.server;

import java.nio.ByteBuffer;

import fr.neatmonster.agaria.GameManager;
import fr.neatmonster.agaria.SocketHandler;
import fr.neatmonster.agaria.packets.PacketFactory;
import fr.neatmonster.agaria.packets.ServerPacket;

public class PacketMessageLength extends ServerPacket {
    public int          length;
    public ServerPacket packet;

    @Override
    public void read(final ByteBuffer buf) {
        length = buf.getInt();

        final byte packetId = buf.get();

        GameManager.logger.finer("RECV packet ID=" + packetId + " LEN=" + length);
        GameManager.logger.finest("dump: " + SocketHandler.toString(buf));

        packet = PacketFactory.createPacket(packetId);
        if (packet == null) {
            GameManager.logger.warning("unknown packet ID(240->" + packetId + ")");
            return;
        }
        packet.read(buf);
    }
}
