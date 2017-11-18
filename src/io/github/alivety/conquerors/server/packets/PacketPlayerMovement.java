package io.github.alivety.conquerors.server.packets;

import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;
import io.github.alivety.ppl.packet.Serverside;

@PacketData(id = 10, desc = "Player Movement", bound = Serverside.class)
public class PacketPlayerMovement extends Packet {
	@PacketField public float x, y, z;
}
