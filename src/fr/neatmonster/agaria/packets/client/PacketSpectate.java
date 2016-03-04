package fr.neatmonster.agaria.packets.client;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketSpectate extends ClientPacket {

    public PacketSpectate() {
        super((byte) (1 & 0xff));
    }
}