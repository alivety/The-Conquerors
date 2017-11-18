package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;
import io.github.alivety.ppl.packet.Serverside;

@PacketData(id = 8, desc = "Send Chat", bound = Serverside.class)
public class P8 extends Packet {
	@PacketField public String message;
}
