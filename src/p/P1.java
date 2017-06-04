package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P1 extends AbstractPacket {
	@PacketField
	private final int id=1;
	@PacketField
	String spatialID;
}
