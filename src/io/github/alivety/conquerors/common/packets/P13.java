package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 13, description = "Disconnect", recv = Serverside.class)
public class P13 extends Packet {
	@PacketField private final int id = 13;
}
