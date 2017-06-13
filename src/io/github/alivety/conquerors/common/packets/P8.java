package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P8 extends Packet {
	@PacketField private final int id = 8;
	@PacketField public String message;
}
