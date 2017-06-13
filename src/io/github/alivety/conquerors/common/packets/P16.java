package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id=16,description="Select Window Slot",recv=Serverside.class)
public class P16 extends Packet {
	@PacketField private final int id = 16;
	@PacketField public String spatialID;
	@PacketField public int slot;
}
