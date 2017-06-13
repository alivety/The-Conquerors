package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.Packet;
import io.github.alivety.ppl.PacketField;

public class P19 extends Packet {
	@PacketField private final int id = 19;
	@PacketField public int money, mpm;
	@PacketField public String[] unitSpatialID;
}
