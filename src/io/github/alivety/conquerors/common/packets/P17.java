package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P17 extends Packet {
	@PacketField private final int id = 17;
	@PacketField public String spatialID, ownerSpatialID;
}
