package fr.neatmonster.agaria.packets.client;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketSplit extends ClientPacket {

    public PacketSplit() {
        super((byte) (17 & 0xff));
    }
}