package io.github.alivety.conquerors.client.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@Deprecated
@PacketData(id = 7, desc = "Rotate Entity", bound = Clientside.class)
public class P7 extends Packet {
	@PacketField public float x, y, z;
}
