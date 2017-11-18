package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 1, desc = "Login Success", bound = Clientside.class)
public class P1 extends Packet {
	@PacketField public String spatialID;
}
