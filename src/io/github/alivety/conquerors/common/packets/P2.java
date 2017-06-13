package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P2 extends Packet {
	@PacketField
	private final int id = 2;
	@PacketField
	public String reason;
}
