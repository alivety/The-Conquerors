package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 19, desc = "Update Player", bound = Clientside.class)
public class P19 extends Packet {
	@PacketField public int money, mpm;
	@PacketField public String[] unitSpatialID;
}
