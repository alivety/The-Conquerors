package io.github.alivety.conquerors.event;

import java.util.HashMap;

import javax.annotation.Nonnull;

import io.github.alivety.conquerors.events.LoginRequestEvent;
import io.github.alivety.conquerors.server.Player;
import io.github.alivety.ppl.AbstractPacket;
import p.*;

public class PacketResolver {
	public Event resolve(@Nonnull AbstractPacket p,@Nonnull Player client) throws IllegalArgumentException, IllegalAccessException {
		int id=p.getPacketID();
		if (id==0) { 
			P0 p0=(P0)p;
			return new LoginRequestEvent(client, p0.username, p0.protocolVersion);
		} else if (id==1) {
			return null;
		}
		
		else {
			throw new NullPointerException("No packet with id="+id);
		}
	}
}
