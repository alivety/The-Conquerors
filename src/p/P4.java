package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P4 extends AbstractPacket {
	@PacketField
	private final int id=4;
	@PacketField
	public String model,material,spatialId;
}
