package io.github.alivety.conquerors.server.packets;

import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;
import io.github.alivety.ppl.packet.Serverside;

@PacketData(id = 14, desc = "Request Window", bound = Serverside.class)
public class P14 extends Packet {
	@PacketField public String spatialID;
}
