package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 4, desc = "Spawn Entity", bound = Clientside.class)
public class P4 extends Packet {
	@PacketField public String model, material, spatialId;
}
