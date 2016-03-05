package fr.neatmonster.agaria.packets.client;

import fr.neatmonster.agaria.packets.ClientPacket;

public class PacketPing extends ClientPacket {

    protected PacketPing() {
        super((byte) (224 & 0xff));
    }
}
