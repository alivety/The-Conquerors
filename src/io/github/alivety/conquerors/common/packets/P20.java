package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 20, desc = "Create Model Spatial", bound = Clientside.class)
public class P20 extends Packet {
	@PacketField public byte shape;
	@PacketField public int[] vectors;
}
