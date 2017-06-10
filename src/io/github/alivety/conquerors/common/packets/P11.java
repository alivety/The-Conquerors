package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P11 extends AbstractPacket {
	@PacketField
	private final int id = 11;
	@PacketField
	public String spatialId;
}
