package io.github.alivety.conquerors.common;

import javax.annotation.Nonnull;

import io.github.alivety.conquerors.client.events.CreateModelEvent;
import io.github.alivety.conquerors.client.events.CreateModelSpatialEvent;
import io.github.alivety.conquerors.client.events.EntityMovedEvent;
import io.github.alivety.conquerors.client.events.EntityOwnershipChangedEvent;
import io.github.alivety.conquerors.client.events.EntityRemovedEvent;
import io.github.alivety.conquerors.client.events.EntityResizedEvent;
import io.github.alivety.conquerors.client.events.EntitySpawnEvent;
import io.github.alivety.conquerors.client.events.LoginFailureEvent;
import io.github.alivety.conquerors.client.events.LoginSuccessEvent;
import io.github.alivety.conquerors.client.events.PlayerListUpdatedEvent;
import io.github.alivety.conquerors.client.events.PlayerVariablesUpdateEvent;
import io.github.alivety.conquerors.client.events.WindowOpenedEvent;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.PlayerChatEvent;
import io.github.alivety.conquerors.common.packets.P0;
import io.github.alivety.conquerors.common.packets.P1;
import io.github.alivety.conquerors.common.packets.P10;
import io.github.alivety.conquerors.common.packets.P11;
import io.github.alivety.conquerors.common.packets.P12;
import io.github.alivety.conquerors.common.packets.P14;
import io.github.alivety.conquerors.common.packets.P15;
import io.github.alivety.conquerors.common.packets.P16;
import io.github.alivety.conquerors.common.packets.P17;
import io.github.alivety.conquerors.common.packets.P18;
import io.github.alivety.conquerors.common.packets.P19;
import io.github.alivety.conquerors.common.packets.P2;
import io.github.alivety.conquerors.common.packets.P20;
import io.github.alivety.conquerors.common.packets.P21;
import io.github.alivety.conquerors.common.packets.P4;
import io.github.alivety.conquerors.common.packets.P5;
import io.github.alivety.conquerors.common.packets.P6;
import io.github.alivety.conquerors.common.packets.P8;
import io.github.alivety.conquerors.common.packets.P9;
import io.github.alivety.conquerors.server.events.LoginRequestEvent;
import io.github.alivety.conquerors.server.events.PlayerDisconnectEvent;
import io.github.alivety.conquerors.server.events.PlayerMoveUnitsEvent;
import io.github.alivety.conquerors.server.events.PlayerMovedEvent;
import io.github.alivety.conquerors.server.events.WindowRequestedEvent;
import io.github.alivety.conquerors.server.events.WindowSlotSelectedEvent;
import io.github.alivety.ppl.packet.Packet;

public class PacketResolver {
	public Event resolve(@Nonnull final Packet p, final PlayerObject client) throws IllegalArgumentException, IllegalAccessException {
		final int id = p.getId();
		if (id == 0) {
			final P0 p0 = (P0) p;
			return new LoginRequestEvent(client, p0.username, p0.protocolVersion);
		} else if (id == 2) {
			final P2 p2 = (P2) p;
			return new LoginFailureEvent(p2.reason);
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
		} else if (id == 19) {
			final P19 p19 = (P19) p;
			return new PlayerVariablesUpdateEvent(p19.money, p19.mpm, p19.unitSpatialID);
		} else if (id == 20) {
			final P20 p20 = (P20) p;
			return new CreateModelSpatialEvent(p20.shape, p20.vectors);
		} else if (id == 21) {
			final P21 p21 = (P21) p;
			return new CreateModelEvent(p21.name, p21.position, p21.form);
		} else
			throw new NullPointerException("No packet with id=" + id);
	}
}
