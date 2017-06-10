package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P7 extends Packet {
	@PacketField
	private final int id = 7;
	@PacketField
	private float x, y, z;
}
