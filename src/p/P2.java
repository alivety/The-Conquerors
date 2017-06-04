package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P2 extends AbstractPacket {
	@PacketField
	private int id=2;
	@PacketField
	private String reason;
}
