package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P15 extends Packet {
	@PacketField
	private final int id = 15;
	@PacketField
	public String[] slots;
}
