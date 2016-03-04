package fr.neatmonster.agaria.packets.client;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketKeyQPressed extends ClientPacket {

    public PacketKeyQPressed() {
        super((byte) (18 & 0xff));
    }
}
