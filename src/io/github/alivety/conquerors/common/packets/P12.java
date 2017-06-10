package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P12 extends Packet {
	@PacketField
	private final int id = 12;
	@PacketField
	public String[] list;
}
