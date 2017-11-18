package io.github.alivety.conquerors.common.packets;

import io.github.alivety.ppl.packet.Packet;
import io.github.alivety.ppl.packet.PacketData;
import io.github.alivety.ppl.packet.Serverside;

@PacketData(id = 13, desc = "Disconnect", bound = Serverside.class)
public class P13 extends Packet {}