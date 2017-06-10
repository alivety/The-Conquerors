package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P0 extends AbstractPacket {
	@PacketField
	private final int id = 0;
	@PacketField
	public String username;
	@PacketField
	public int protocolVersion;
}
