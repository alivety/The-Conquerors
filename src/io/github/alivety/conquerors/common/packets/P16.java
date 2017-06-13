package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P16 extends Packet {
	@PacketField private final int id = 16;
	@PacketField public String spatialID;
	@PacketField public int slot;
}
