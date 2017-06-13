package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 0, description = "Login Request", recv = Serverside.class)
public class P0 extends Packet {
	@PacketField private final int id = 0;
	@PacketField public String username;
	@PacketField public int protocolVersion;
}
