package fr.neatmonster.agaria.packets.client;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketEjectMass extends ClientPacket {

    public PacketEjectMass() {
        super((byte) (21 & 0xff));
    }
}