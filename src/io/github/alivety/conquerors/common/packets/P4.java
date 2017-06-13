package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 4, description = "Spawn Entity", recv = Clientside.class)
public class P4 extends Packet {
	@PacketField private final int id = 4;
	@PacketField public String model, material, spatialId;
}
