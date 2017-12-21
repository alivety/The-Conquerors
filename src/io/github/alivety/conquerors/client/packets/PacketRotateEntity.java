package io.github.alivety.conquerors.client.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 7, desc = "Rotate Entity (Set Look)", bound = Clientside.class)
public class PacketRotateEntity extends Packet {
	@PacketField public String spatialID;
	@PacketField public float x, y, z;
	@PacketField public float ux,uy,uz;
}
