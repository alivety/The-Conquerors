package io.github.alivety.conquerors.client.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 4, desc = "Spawn Entity", bound = Clientside.class)
public class PacketSpawnEntity extends Packet {
	@PacketField public String model, material, spatialId;
}
