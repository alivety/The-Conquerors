package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P3 extends AbstractPacket {
	@PacketField
	private final int id = 3;
	@PacketField
	private String model;
}
