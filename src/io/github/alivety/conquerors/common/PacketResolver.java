package io.github.alivety.conquerors.common;

import javax.annotation.Nonnull;

import io.github.alivety.conquerors.client.events.EntityMovedEvent;
import io.github.alivety.conquerors.client.events.EntityOwnershipChangedEvent;
import io.github.alivety.conquerors.client.events.EntityRemovedEvent;
import io.github.alivety.conquerors.client.events.EntityResizedEvent;
import io.github.alivety.conquerors.client.events.EntitySpawnEvent;
import io.github.alivety.conquerors.client.events.LoginSuccessEvent;
import io.github.alivety.conquerors.client.events.PlayerListUpdatedEvent;
import io.github.alivety.conquerors.client.events.WindowOpenedEvent;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.PlayerChatEvent;
import io.github.alivety.conquerors.server.Player;
import io.github.alivety.conquerors.server.events.LoginRequestEvent;
import io.github.alivety.conquerors.server.events.PlayerDisconnectEvent;
import io.github.alivety.conquerors.server.events.PlayerMoveUnitsEvent;
import io.github.alivety.conquerors.server.events.PlayerMovedEvent;
import io.github.alivety.conquerors.server.events.WindowRequestedEvent;
import io.github.alivety.conquerors.server.events.WindowSlotSelectedEvent;
import io.github.alivety.ppl.AbstractPacket;
import p.P0;
import p.P1;
import p.P10;
import p.P11;
import p.P12;
import p.P14;
import p.P15;
import p.P16;
import p.P17;
import p.P18;
import p.P4;
import p.P5;
import p.P6;
import p.P8;
import p.P9;

public class PacketResolver {
	public Event resolve(@Nonnull final AbstractPacket p, final Player client)
			throws IllegalArgumentException, IllegalAccessException {
		final int id = p.getPacketID();
		if (id == 0) {
			final P0 p0 = (P0) p;
			return new LoginRequestEvent(client, p0.username, p0.protocolVersion);
		} else if (id == 1) {
			final P1 p1 = (P1) p;
			return new LoginSuccessEvent(p1.spatialID);
		} else if (id == 4) {
			final P4 p4 = (P4) p;
			return new EntitySpawnEvent(p4.model, p4.material, p4.spatialId);
		} else if (id == 5) {
			final P5 p5 = (P5) p;
			return new EntityMovedEvent(p5.spatialID, p5.x, p5.y, p5.z);
		} else if (id == 6) {
			final P6 p6 = (P6) p;
			return new EntityResizedEvent(p6.spatialID, p6.x, p6.y, p6.z);
		} else if (id == 8) {
			final P8 p8 = (P8) p;
			return new PlayerChatEvent(client, p8.message);
		} else if (id == 9) {
			final P9 p9 = (P9) p;
			return new PlayerChatEvent(null, p9.raw_msg);
		} else if (id == 10) {
			final P10 p10 = (P10) p;
			return new PlayerMovedEvent(client, p10.x, p10.y, p10.z);
		} else if (id == 11) {
			final P11 p11 = (P11) p;
			return new EntityRemovedEvent(p11.spatialId);
		} else if (id == 12) {
			final P12 p12 = (P12) p;
			return new PlayerListUpdatedEvent(p12.list);
		} else if (id == 13)
			return new PlayerDisconnectEvent(client);
		else if (id == 14) {
			final P14 p14 = (P14) p;
			return new WindowRequestedEvent(client, p14.spatialID);
		} else if (id == 15) {
			final P15 p15 = (P15) p;
			return new WindowOpenedEvent(p15.slots);
		} else if (id == 16) {
			final P16 p16 = (P16) p;
			return new WindowSlotSelectedEvent(client, p16.spatialID, p16.slot);
		} else if (id == 17) {
			final P17 p17 = (P17) p;
			return new EntityOwnershipChangedEvent(p17.spatialID, p17.ownerSpatialID);
		} else if (id == 18) {
			final P18 p18 = (P18) p;
			return new PlayerMoveUnitsEvent(client, p18.spatialID, p18.x, p18.y, p18.z);
		} else
			throw new NullPointerException("No packet with id=" + id);
	}
}
