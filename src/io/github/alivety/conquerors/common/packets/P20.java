package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id=20,description="Create Model Spatial",recv=Clientside.class)
public class P20 extends Packet {
	@PacketField private final int id=20;
	@PacketField public byte shape;
	@PacketField public int[] vectors;
}
