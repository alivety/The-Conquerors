package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 6, description = "Scale Entity", recv = Clientside.class)
public class P6 extends Packet {
	@PacketField private final int id = 6;
	@PacketField public String spatialID;
	@PacketField public float x, y, z;
}
