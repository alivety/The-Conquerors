package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P6 extends Packet {
	@PacketField private final int id = 6;
	@PacketField public String spatialID;
	@PacketField public float x, y, z;
}
