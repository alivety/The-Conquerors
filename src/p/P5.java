package p;

import io.github.alivety.ppl.AbstractPacket;
import io.github.alivety.ppl.PacketField;

public class P5 extends AbstractPacket {
	@PacketField
	private final int id=5;
	@PacketField
	private String spatialID;
	@PacketField
	private float x,y,z;
}