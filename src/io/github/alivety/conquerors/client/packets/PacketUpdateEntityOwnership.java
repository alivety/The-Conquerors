package io.github.alivety.conquerors.client.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 17, desc = "Updated Entity Ownership", bound = Clientside.class)
public class PacketUpdateEntityOwnership extends Packet {
	@PacketField public String spatialID, ownerSpatialID;
}
