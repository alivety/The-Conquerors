package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 12, description = "Player List", recv = Clientside.class) public class P12 extends Packet {
	@PacketField private final int id = 12;
	@PacketField public String[] list;
}
