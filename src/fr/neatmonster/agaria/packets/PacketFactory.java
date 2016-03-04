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
        registerPacket(PacketAddCell.class);
        registerPacket(PacketClearCells.class);
        registerPacket(PacketDrawLine.class);
        registerPacket(PacketExperienceInfo.class);
        registerPacket(PacketSetBorder.class);
        registerPacket(PacketUpdateCells.class);
        registerPacket(PacketUpdateLeaderboard.class);
        registerPacket(PacketUpdateView.class);
    }

    private static Map<Byte, Class<? extends ServerPacket>> SERVER_PACKETS = new HashMap<>();

    private static void registerPacket(
            final Class<? extends ServerPacket> packetClass) {
        try {
            final ServerPacket packet = packetClass.newInstance();
            SERVER_PACKETS.put(packet.getPacketId(), packetClass);
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
