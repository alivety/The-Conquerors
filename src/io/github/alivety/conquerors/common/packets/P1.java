package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 1, description = "Login Success", recv = Clientside.class)
public class P1 extends Packet {
	@PacketField private final int id = 1;
	@PacketField public String spatialID;
}
