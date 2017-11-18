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
import io.github.alivety.conquerors.client.packets.PacketChatMessage;
import io.github.alivety.conquerors.client.packets.PacketCreateModel;
import io.github.alivety.conquerors.client.packets.PacketCreateModelSpatial;
import io.github.alivety.conquerors.client.packets.PacketLoginFailure;
import io.github.alivety.conquerors.client.packets.PacketLoginSuccess;
import io.github.alivety.conquerors.client.packets.PacketOpenWindow;
import io.github.alivety.conquerors.client.packets.PacketPlayerList;
import io.github.alivety.conquerors.client.packets.PacketRemoveEntity;
import io.github.alivety.conquerors.client.packets.PacketScaleEntity;
import io.github.alivety.conquerors.client.packets.PacketSpawnEntity;
import io.github.alivety.conquerors.client.packets.PacketTranslateEntity;
import io.github.alivety.conquerors.client.packets.PacketUpdateEntityOwnership;
import io.github.alivety.conquerors.client.packets.PacketUpdatePlayer;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.PlayerChatEvent;
import io.github.alivety.conquerors.server.events.LoginRequestEvent;
import io.github.alivety.conquerors.server.events.PlayerDisconnectEvent;
import io.github.alivety.conquerors.server.events.PlayerMoveUnitsEvent;
import io.github.alivety.conquerors.server.events.PlayerMovedEvent;
import io.github.alivety.conquerors.server.events.WindowRequestedEvent;
import io.github.alivety.conquerors.server.events.WindowSlotSelectedEvent;
import io.github.alivety.conquerors.server.packets.PacketLoginRequest;
import io.github.alivety.conquerors.server.packets.PacketMoveUnits;
import io.github.alivety.conquerors.server.packets.PacketPlayerMovement;
import io.github.alivety.conquerors.server.packets.PacketRequestWindow;
import io.github.alivety.conquerors.server.packets.PacketSelectWindowSlot;
import io.github.alivety.conquerors.server.packets.PacketSendChat;
import io.github.alivety.ppl.packet.Packet;

public class PacketResolver {
	public Event resolve(@Nonnull final Packet p, final PlayerObject client) throws IllegalArgumentException, IllegalAccessException {
		final int id = p.getId();
		if (id == 0) {
			final PacketLoginRequest p0 = (PacketLoginRequest) p;
			return new LoginRequestEvent(client, p0.username, p0.protocolVersion);
		} else if (id == 2) {
			final PacketLoginFailure p2 = (PacketLoginFailure) p;
			return new LoginFailureEvent(p2.reason);
		} else if (id == 1) {
			final PacketLoginSuccess p1 = (PacketLoginSuccess) p;
			return new LoginSuccessEvent(p1.spatialID);
		} else if (id == 4) {
			final PacketSpawnEntity p4 = (PacketSpawnEntity) p;
			return new EntitySpawnEvent(p4.model, p4.material, p4.spatialId);
		} else if (id == 5) {
			final PacketTranslateEntity p5 = (PacketTranslateEntity) p;
			return new EntityMovedEvent(p5.spatialID, p5.x, p5.y, p5.z);
		} else if (id == 6) {
			final PacketScaleEntity p6 = (PacketScaleEntity) p;
			return new EntityResizedEvent(p6.spatialID, p6.x, p6.y, p6.z);
		} else if (id == 8) {
			final PacketSendChat p8 = (PacketSendChat) p;
			return new PlayerChatEvent(client, p8.message);
		} else if (id == 9) {
			final PacketChatMessage p9 = (PacketChatMessage) p;
			return new PlayerChatEvent(null, p9.raw_msg);
		} else if (id == 10) {
			final PacketPlayerMovement p10 = (PacketPlayerMovement) p;
			return new PlayerMovedEvent(client, p10.x, p10.y, p10.z);
		} else if (id == 11) {
			final PacketRemoveEntity p11 = (PacketRemoveEntity) p;
			return new EntityRemovedEvent(p11.spatialId);
		} else if (id == 12) {
			final PacketPlayerList p12 = (PacketPlayerList) p;
			return new PlayerListUpdatedEvent(p12.list);
		} else if (id == 13)
			return new PlayerDisconnectEvent(client);
		else if (id == 14) {
			final PacketRequestWindow p14 = (PacketRequestWindow) p;
			return new WindowRequestedEvent(client, p14.spatialID);
		} else if (id == 15) {
			final PacketOpenWindow p15 = (PacketOpenWindow) p;
			return new WindowOpenedEvent(p15.slots);
		} else if (id == 16) {
			final PacketSelectWindowSlot p16 = (PacketSelectWindowSlot) p;
			return new WindowSlotSelectedEvent(client, p16.spatialID, p16.slot);
		} else if (id == 17) {
			final PacketUpdateEntityOwnership p17 = (PacketUpdateEntityOwnership) p;
			return new EntityOwnershipChangedEvent(p17.spatialID, p17.ownerSpatialID);
		} else if (id == 18) {
			final PacketMoveUnits p18 = (PacketMoveUnits) p;
			return new PlayerMoveUnitsEvent(client, p18.spatialID, p18.x, p18.y, p18.z);
		} else if (id == 19) {
			final PacketUpdatePlayer p19 = (PacketUpdatePlayer) p;
			return new PlayerVariablesUpdateEvent(p19.money, p19.mpm, p19.unitSpatialID);
		} else if (id == 20) {
			final PacketCreateModelSpatial p20 = (PacketCreateModelSpatial) p;
			return new CreateModelSpatialEvent(p20.shape, p20.vectors);
		} else if (id == 21) {
			final PacketCreateModel p21 = (PacketCreateModel) p;
			return new CreateModelEvent(p21.name, p21.position, p21.form);
		} else
			throw new NullPointerException("No packet with id=" + id);
	}
}
