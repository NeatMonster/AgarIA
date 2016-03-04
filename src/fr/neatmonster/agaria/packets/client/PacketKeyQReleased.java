package fr.neatmonster.agaria.packets.client;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketKeyQReleased extends ClientPacket {

    public PacketKeyQReleased() {
        super((byte) (19 & 0xff));
    }
}
