package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P19 extends AbstractPacket {
	@PacketField
	private final int id=19;
	@PacketField
	private int money,mpm;
	@PacketField
	String[] unitSpatialID;
}
