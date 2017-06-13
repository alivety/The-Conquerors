package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 8, description = "Send Chat", recv = Serverside.class)
public class P8 extends Packet {
	@PacketField private final int id = 8;
	@PacketField public String message;
}
