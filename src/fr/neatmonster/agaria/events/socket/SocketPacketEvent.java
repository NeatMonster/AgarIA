package fr.neatmonster.agaria.events.socket;

import fr.neatmonster.agaria.packets.ServerPacket;

public class SocketPacketEvent extends SocketEvent {
    public final ServerPacket packet;

    public SocketPacketEvent(final ServerPacket packet) {
        this.packet = packet;
    }
}
