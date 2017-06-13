package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 19, description = "Update Player", recv = Clientside.class) public class P19 extends Packet {
	@PacketField private final int id = 19;
	@PacketField public int money, mpm;
	@PacketField public String[] unitSpatialID;
}
