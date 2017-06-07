package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P15 extends AbstractPacket {
	@PacketField
	private final int id=15;
	@PacketField
	public String[] slots;
}
