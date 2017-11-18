package io.github.alivety.conquerors.server.packets;

import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;
import io.github.alivety.ppl.packet.Serverside;

@PacketData(id = 0, desc = "Login Request", bound = Serverside.class)
public class P0 extends Packet {
	@PacketField public String username;
	@PacketField public int protocolVersion;
}
