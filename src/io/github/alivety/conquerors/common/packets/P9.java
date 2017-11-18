package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 9, desc = "Chat Message", bound = Clientside.class)
public class P9 extends Packet {
	@PacketField public String raw_msg;
}
