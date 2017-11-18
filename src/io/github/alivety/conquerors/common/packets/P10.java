package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;
import io.github.alivety.ppl.packet.Serverside;

@PacketData(id = 10, desc = "Player Movement", bound = Serverside.class)
public class P10 extends Packet {
	@PacketField public float x, y, z;
}
