package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P10 extends AbstractPacket {
	@PacketField
	private final int id = 10;
	@PacketField
	public float x, y, z;
}
