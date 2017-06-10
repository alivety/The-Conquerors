package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P3 extends Packet {
	@PacketField
	private final int id = 3;
	@PacketField
	private String model;
}
