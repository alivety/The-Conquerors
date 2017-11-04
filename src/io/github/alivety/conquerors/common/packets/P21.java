package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 21, description = "Create Model", recv = Clientside.class)
public class P21 extends Packet {
	@PacketField private final int id = 21;
	@PacketField public String name;
	@PacketField public int[] position;
	@PacketField public int[][] form;
}
