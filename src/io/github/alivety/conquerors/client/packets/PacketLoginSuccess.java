package io.github.alivety.conquerors.client.packets;

import com.jme3.math.ColorRGBA;

import io.github.alivety.ppl.packet.Clientside;
import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.PacketField;

@PacketData(id = 1, desc = "Login Success", bound = Clientside.class)
public class PacketLoginSuccess extends Packet {
	@PacketField public String spatialID;
	@PacketField public ColorRGBA team;
}
