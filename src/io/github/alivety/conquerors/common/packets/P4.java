package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P4 extends Packet {
	@PacketField
	private final int id = 4;
	@PacketField
	public String model, material, spatialId;
}
