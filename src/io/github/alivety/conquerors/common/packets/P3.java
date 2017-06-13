package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@Deprecated
@PacketData(id = 3, description = "Check Model", recv = Clientside.class)
public class P3 extends Packet {
	@PacketField private final int id = 3;
	@PacketField public String model;
}
