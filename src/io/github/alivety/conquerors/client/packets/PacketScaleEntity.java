package io.github.alivety.conquerors.client.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 6, desc = "Scale Entity", bound = Clientside.class)
public class PacketScaleEntity extends Packet {
	@PacketField public String spatialID;
	@PacketField public float x, y, z;
}
