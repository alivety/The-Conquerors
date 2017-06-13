package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id=2,description="Login Failure",recv=Clientside.class)
public class P2 extends Packet {
	@PacketField private final int id = 2;
	@PacketField public String reason;
}
