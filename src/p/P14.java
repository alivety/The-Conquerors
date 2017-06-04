package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P14 extends AbstractPacket {
	@PacketField
	private final int id=14;
	@PacketField
	private String spatialID;
}
