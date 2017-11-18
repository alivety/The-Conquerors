package io.github.alivety.conquerors.client.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@Deprecated
@PacketData(id = 3, desc = "Check Model", bound = Clientside.class)
public class P3 extends Packet {
	@PacketField public String model;
}
