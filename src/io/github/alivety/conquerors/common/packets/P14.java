package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P14 extends Packet {
	@PacketField
	private final int id = 14;
	@PacketField
	public String spatialID;
}
