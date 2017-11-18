package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 5, desc = "Translate Entity", bound = Clientside.class)
public class P5 extends Packet {
	@PacketField public String spatialID;
	@PacketField public float x, y, z;
}
