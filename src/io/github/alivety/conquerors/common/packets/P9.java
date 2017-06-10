package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P9 extends AbstractPacket {
	@PacketField
	private final int id = 9;
	@PacketField
	public String raw_msg;
}
