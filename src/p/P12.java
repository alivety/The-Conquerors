package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P12 extends AbstractPacket {
	@PacketField
	private final int id = 12;
	@PacketField
	public String[] list;
}
