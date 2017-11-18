package io.github.alivety.conquerors.client.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 21, desc = "Create Model", bound = Clientside.class)
public class P21 extends Packet {
	@PacketField public String name;
	@PacketField public int[] position;
	@PacketField public int[][] form;
}
