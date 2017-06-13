package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 11, description = "Remove Entity")
public class P11 extends Packet {
	private final @PacketField int id = 11;
	public @PacketField String spatialId;
}
