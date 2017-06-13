package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P18 extends Packet {
	@PacketField private final int id = 18;
	@PacketField public String[] spatialID;
	@PacketField public float x, y, z;
}
