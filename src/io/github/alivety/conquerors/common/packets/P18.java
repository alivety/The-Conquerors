package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 18, description = "Move Units", recv = Serverside.class)
public class P18 extends Packet {
	@PacketField private final int id = 18;
	@PacketField public String[] spatialID;
	@PacketField public float x, y, z;
}
