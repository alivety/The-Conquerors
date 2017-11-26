package io.github.alivety.conquerors.client.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 21, desc = "Create Model", bound = Clientside.class)
public class PacketCreateModel extends Packet {
	@PacketField public String spatialID;
	@PacketField public float[] position;
	@PacketField public float[][] form;
}
