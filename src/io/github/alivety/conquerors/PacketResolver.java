package io.github.alivety.conquerors;

import java.util.HashMap;

import javax.annotation.Nonnull;

import io.github.alivety.conquerors.event.Event;
import io.github.alivety.conquerors.events.*;
import io.github.alivety.conquerors.server.Player;
import io.github.alivety.ppl.AbstractPacket;
import p.*;

public class PacketResolver {
	public Event resolve(@Nonnull AbstractPacket p,Player client) throws IllegalArgumentException, IllegalAccessException {
		int id=p.getPacketID();
		if (id==0) { 
			P0 p0=(P0)p;
			return new LoginRequestEvent(client, p0.username, p0.protocolVersion);
		} else if (id==1) {
			P1 p1=(P1)p;
			return new LoginSuccessEvent(p1.spatialID);
		} else if (id==4) {
			P4 p4=(P4)p;
			return new EntitySpawnEvent(p4.model,p4.material,p4.spatialId);
		} else if (id==5) {
			P5 p5=(P5)p;
			return new EntityMovedEvent(p5.spatialID,p5.x,p5.y,p5.z);
		} else if (id==6) {
			P6 p6=(P6)p;
			return new EntityResizedEvent(p6.spatialID,p6.x,p6.y,p6.z);
		} else if (id==8) {
			P8 p8=(P8)p;
			return new PlayerChatEvent(client,p8.message);
		} else if (id==9) {
			P9 p9=(P9)p;
			return new PlayerChatEvent(null,p9.raw_msg);
		} else if (id==10) {
			P10 p10=(P10)p;
			return new PlayerMovedEvent(client,p10.x,p10.y,p10.z);
		} else if (id==11) {
			P11 p11=(P11)p;
			return new EntityRemovedEvent(p11.spatialId);
		} else if (id==12) {
			P12 p12=(P12)p;
			return new PlayerListUpdatedEvent(p12.list);
		} else if (id==13) {
			return new PlayerDisconnectEvent(client);
		} else if (id==14) {
			P14 p14=(P14)p;
			return new WindowRequestedEvent(client,p14.spatialID);
		} else if (id==15) {
			P15 p15=(P15)p;
			return new WindowOpenedEvent(p15.slots);
		} else if (id==16) {
			P16 p16=(P16)p;
			return new WindowSlotSelectedEvent(client,p16.spatialID,p16.slot);
		} else if (id==17) {
			P17 p17=(P17)p;
			return new EntityOwnershipChangedEvent(p17.spatialID,p17.ownerSpatialID);
		} else if (id==18) {
			P18 p18=(P18)p;
			return new PlayerMoveUnitsEvent(client,p18.spatialID,p18.x,p18.y,p18.z);
		}
		
		else {
			throw new NullPointerException("No packet with id="+id);
		}
	}
}
