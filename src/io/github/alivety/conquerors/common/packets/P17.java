package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id=17,description="Updated Entity Ownership",recv=Clientside.class)
public class P17 extends Packet {
	@PacketField private final int id = 17;
	@PacketField public String spatialID, ownerSpatialID;
}
