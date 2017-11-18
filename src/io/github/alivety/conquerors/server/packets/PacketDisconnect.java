package io.github.alivety.conquerors.server.packets;

import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.Serverside;

@PacketData(id = 13, desc = "Disconnect", bound = Serverside.class)
public class PacketDisconnect extends Packet {}