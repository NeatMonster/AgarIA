package fr.neatmonster.agaria.packets;

import java.util.HashMap;
import java.util.Map;

import fr.neatmonster.agaria.packets.server.PacketAddCell;
import fr.neatmonster.agaria.packets.server.PacketClearCells;
import fr.neatmonster.agaria.packets.server.PacketDrawDebugLine;
import fr.neatmonster.agaria.packets.server.PacketExperienceInfo;
import fr.neatmonster.agaria.packets.server.PacketGameEnd;
import fr.neatmonster.agaria.packets.server.PacketGotLogin;
import fr.neatmonster.agaria.packets.server.PacketLogout;
import fr.neatmonster.agaria.packets.server.PacketMessageLength;
import fr.neatmonster.agaria.packets.server.PacketPong;
import fr.neatmonster.agaria.packets.server.PacketSetBorder;
import fr.neatmonster.agaria.packets.server.PacketUpdateCells;
import fr.neatmonster.agaria.packets.server.PacketUpdateLeaderboard;
import fr.neatmonster.agaria.packets.server.PacketUpdateTeamsScores;
import fr.neatmonster.agaria.packets.server.PacketUpdateView;

public class PacketFactory {
    private static Map<Byte, Class<? extends ServerPacket>> SERVER_PACKETS = new HashMap<>();

    static {
        registerPacket(16, PacketUpdateCells.class);
        registerPacket(17, PacketUpdateView.class);
        registerPacket(18, PacketClearCells.class);
        registerPacket(21, PacketDrawDebugLine.class);
        registerPacket(32, PacketAddCell.class);
        registerPacket(49, PacketUpdateLeaderboard.class);
        registerPacket(50, PacketUpdateTeamsScores.class);
        registerPacket(64, PacketSetBorder.class);
        registerPacket(81, PacketExperienceInfo.class);
        registerPacket(103, PacketGotLogin.class);
        registerPacket(104, PacketLogout.class);
        registerPacket(225, PacketPong.class);
        registerPacket(240, PacketMessageLength.class);
        registerPacket(254, PacketGameEnd.class);
    }

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
