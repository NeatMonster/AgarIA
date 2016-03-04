package fr.neatmonster.agaria.packets;

import java.util.HashMap;
import java.util.Map;

import fr.neatmonster.agaria.packets.server.PacketAddCell;
import fr.neatmonster.agaria.packets.server.PacketClearCells;
import fr.neatmonster.agaria.packets.server.PacketDrawLine;
import fr.neatmonster.agaria.packets.server.PacketExperienceInfo;
import fr.neatmonster.agaria.packets.server.PacketSetBorder;
import fr.neatmonster.agaria.packets.server.PacketUpdateCells;
import fr.neatmonster.agaria.packets.server.PacketUpdateLeaderboard;
import fr.neatmonster.agaria.packets.server.PacketUpdateView;

public class PacketFactory {
    static {
        registerPacket(32, PacketAddCell.class);
        registerPacket(18, PacketClearCells.class);
        registerPacket(21, PacketDrawLine.class);
        registerPacket(81, PacketExperienceInfo.class);
        registerPacket(64, PacketSetBorder.class);
        registerPacket(16, PacketUpdateCells.class);
        registerPacket(49, PacketUpdateLeaderboard.class);
        registerPacket(17, PacketUpdateView.class);
    }

    private static Map<Byte, Class<? extends ServerPacket>> SERVER_PACKETS = new HashMap<>();

    private static void registerPacket(final int packetId, final Class<? extends ServerPacket> packetClass) {
        try {
            SERVER_PACKETS.put((byte) (packetId & 0xff), packetClass);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static ServerPacket createPacket(final byte packetId) {
        if (SERVER_PACKETS.containsKey(packetId))
            try {
                return SERVER_PACKETS.get(packetId).newInstance();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return null;
    }
}
