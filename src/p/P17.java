package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P17 extends AbstractPacket {
	@PacketField
	private final int id=17;
	@PacketField
	public String spatialID,ownerSpatialID;
}
