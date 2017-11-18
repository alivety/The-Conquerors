package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 11, desc = "Remove Entity", bound = Clientside.class)
public class P11 extends Packet {
	public @PacketField String spatialId;
}
