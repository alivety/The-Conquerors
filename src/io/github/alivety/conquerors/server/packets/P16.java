package io.github.alivety.conquerors.server.packets;

import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;
import io.github.alivety.ppl.packet.Serverside;

@PacketData(id = 16, desc = "Select Window Slot", bound = Serverside.class)
public class P16 extends Packet {
	@PacketField public String spatialID;
	@PacketField public int slot;
}
