package io.github.alivety.conquerors.server;

import static io.github.alivety.conquerors.common.event.EventPriority.SYS;

import java.util.Arrays;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.UnitObject;
import io.github.alivety.conquerors.common.event.SubscribeEvent;
import io.github.alivety.conquerors.common.events.PlayerChatEvent;
import io.github.alivety.conquerors.server.events.LoginRequestEvent;
import io.github.alivety.conquerors.server.events.PlayerDisconnectEvent;
import io.github.alivety.conquerors.server.events.PlayerMoveUnitsEvent;
import io.github.alivety.conquerors.server.events.PlayerMovedEvent;
import io.github.alivety.conquerors.server.events.WindowRequestedEvent;
import io.github.alivety.conquerors.server.events.WindowSlotSelectedEvent;
import io.github.alivety.conquerors.server.windows.WindowAlly;

public class ServerEventSubscriber {
	private final Server server;
	
	public ServerEventSubscriber(final Server server) {
		this.server = server;
	}
	
	@SubscribeEvent(SYS)
	public void onLoginRequest(final LoginRequestEvent evt) {
		if (evt.protocolVersion != Main.PRO_VER) {
			evt.client.write(Main.createPacket(2, "This server is on version " + Main.PRO_VER));
			return;
		}
		if (Arrays.asList(this.server.playerList()).contains(evt.username)) {
			evt.client.packet(2, "A player with that username is already connected");
			return;
		}
		if (server.getOnlinePlayers().length==10) {
			evt.client.packet(2, "There are already 10 players connected");
			return;
		}
		
		evt.client.username(evt.username);
		evt.client.packet(1, evt.client.getSpatialID());
		this.server.broadcast(Main.createPacket(12, new Object[] { this.server.playerList() }));
		this.server.broadcast(Main.createPacket(9, Main.formatChatMessage(evt.username + " has joined the game")));
		evt.client.packet(9, Main.formatChatMessage(evt.username + ".spatialID=" + evt.client.getSpatialID()));
		
		if (server.getOnlinePlayers().length<3) {
			server.broadcast(Main.createPacket(9, Main.formatChatMessage("There must be "+(3-server.getOnlinePlayers().length)+" more players to start the game")));
			server.begin=false;
		}
		
		// TODO spawn starter items
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerChat(final PlayerChatEvent evt) {
		if (evt.client == null)
			return;
		this.server.broadcast(Main.createPacket(9, Main.formatChatMessage(evt.client.username(), evt.message)));
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerDisconnect(final PlayerDisconnectEvent evt) {
		this.server.broadcast(Main.createPacket(9, Main.formatChatMessage(evt.player.username() + " has left the game")));
		this.server.broadcast(Main.createPacket(9, Main.formatChatMessage("Their assets will now be liquidated")));
		for (final UnitObject u : evt.player.getUnits()) {
			this.server.broadcast(Main.createPacket(11, u.getSpatialID()));
			this.server.unregister(u);
		}
		server.players.remove(evt.player);
		evt.player.username(null);
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerMove(final PlayerMovedEvent evt) {
		Main.out.debug("Players are not spawned");
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerMoveUnits(final PlayerMoveUnitsEvent evt) {
		if (!server.begin) {
			evt.player.packet(9, Main.formatChatMessage("You cannot move units until the game has began"));
			return;
		}
		//TODO player move units
	}
	
	@SubscribeEvent(SYS)
	public void onSlotSelected(final WindowSlotSelectedEvent evt) {
		server.windowByID(evt.spatialID).getSlots()[evt.slot].click();
	}
	
	@SubscribeEvent(SYS)
	public void onWindowRequest(final WindowRequestedEvent evt) {
		switch (evt.spatialID) {
			case "ally":
				server.scheduleWindowOpen(evt.player, new WindowAlly(evt,server));
				break;
				
			default:
				if (!server.begin) {
					evt.player.packet(9, Main.formatChatMessage("You cannot move units until the game has began"));
					return;
				}
				//TODO window by unit
		}
	}
}
