package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@Deprecated
@PacketData(id = 10, description = "Player Movement")
public class P10 extends Packet {
	@PacketField private final int id = 10;
	@PacketField public float x, y, z;
}
