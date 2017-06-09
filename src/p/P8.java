package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P8 extends AbstractPacket {
	@PacketField
	private final int id = 8;
	@PacketField
	public String message;
}
