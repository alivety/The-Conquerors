package io.github.alivety.conquerors.common.packets;

import io.github.alivety.conquerors.common.events.Clientside;
import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

@Deprecated
@PacketData(id = 7, description = "Rotate Entity", recv = Clientside.class)
public class P7 extends Packet {
	@PacketField private final int id = 7;
	@PacketField public float x, y, z;
}
