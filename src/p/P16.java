package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P16 extends AbstractPacket {
	@PacketField
	private final int id = 16;
	@PacketField
	public String spatialID;
	@PacketField
	public int slot;
}
