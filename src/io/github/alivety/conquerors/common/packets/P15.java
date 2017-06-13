package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@PacketData(id = 15, description = "Open Window", recv = Clientside.class)
public class P15 extends Packet {
	@PacketField private final int id = 15;
	@PacketField public String[] slots;
}
