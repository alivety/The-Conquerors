package io.github.alivety.conquerors.server.packets;

import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;
import io.github.alivety.ppl.packet.Serverside;

@PacketData(id = 18, desc = "Move Units", bound = Serverside.class)
public class PacketMoveUnits extends Packet {
	@PacketField public String[] spatialID;
	@PacketField public float x, y, z;
}
