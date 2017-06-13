package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 14, description = "Request Window", recv = Serverside.class) public class P14 extends Packet {
	@PacketField private final int id = 14;
	@PacketField public String spatialID;
}
