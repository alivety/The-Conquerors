package io.github.alivety.conquerors.client.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 15, desc = "Open Window", bound = Clientside.class)
public class PacketOpenWindow extends Packet {
	@PacketField public String spatialID;
	@PacketField public String[] slots;
}
