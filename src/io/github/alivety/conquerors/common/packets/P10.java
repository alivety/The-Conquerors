package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 10, description = "Player Movement", recv = Serverside.class)
public class P10 extends Packet {
	@PacketField private final int id = 10;
	@PacketField public float x, y, z;
}
