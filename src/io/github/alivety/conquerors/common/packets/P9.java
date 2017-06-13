package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 9, description = "Chat Message", recv = Clientside.class) public class P9 extends Packet {
	@PacketField private final int id = 9;
	@PacketField public String raw_msg;
}
