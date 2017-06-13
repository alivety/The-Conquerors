package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 5, description = "Translate Entity", recv = Clientside.class)
public class P5 extends Packet {
	@PacketField private final int id = 5;
	@PacketField public String spatialID;
	@PacketField public float x, y, z;
}
