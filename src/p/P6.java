package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P6 extends AbstractPacket {
	@PacketField
	private final int id=6;
	@PacketField
	private float x,y,z;
}