package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 1, description = "Login Success")
public class P1 extends Packet {
	@PacketField private final int id = 1;
	@PacketField public String spatialID;
}
