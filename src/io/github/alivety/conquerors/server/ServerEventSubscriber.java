package io.github.alivety.conquerors.server;

import static io.github.alivety.conquerors.event.EventPriority.SYS;

import java.util.Arrays;

import io.github.alivety.conquerors.Main;
import io.github.alivety.conquerors.event.SubscribeEvent;
import io.github.alivety.conquerors.events.LoginRequestEvent;
import io.github.alivety.conquerors.events.PlayerChatEvent;
import io.github.alivety.conquerors.events.PlayerDisconnectEvent;
import io.github.alivety.conquerors.events.PlayerMoveUnitsEvent;
import io.github.alivety.conquerors.events.PlayerMovedEvent;
import io.github.alivety.conquerors.events.WindowRequestedEvent;
import io.github.alivety.conquerors.events.WindowSlotSelectedEvent;

public class ServerEventSubscriber {
	private final Server server;
	public ServerEventSubscriber(Server server) {
		this.server=server;
	}
	
	@SubscribeEvent(SYS)
	public void onLoginRequest(LoginRequestEvent evt) {
		if (evt.protocolVersion!=Main.PRO_VER) {
			evt.client.write(Main.createPacket(2, "This server is on version "+Main.PRO_VER));
			return;
		}
		if (Arrays.asList(server.playerList()).contains(evt.username)) {
			evt.client.packet(2, "A player with that username is already connected");
			return;
		}
		
		evt.client.username=evt.username;
		evt.client.packet(1, evt.client.spatialID);
		server.broadcast(Main.createPacket(12, new Object[]{server.playerList()}));
		server.broadcast(Main.createPacket(9, Main.formatChatMessage(evt.username+" has joined the game")));
		evt.client.packet(9, Main.formatChatMessage(evt.username+".spatialID="+evt.client.spatialID));
		
		//spawn starter items
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerChat(PlayerChatEvent evt) {
		if (evt.client==null) return;
		server.broadcast(Main.createPacket(9, Main.formatChatMessage(evt.client.username, evt.message)));
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerMove(PlayerMovedEvent evt) {
		Main.out.debug("Players are not spawned");
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerDisconnect(PlayerDisconnectEvent evt) {
		server.broadcast(Main.createPacket(9, Main.formatChatMessage(evt.player.username+" has left the game")));
		server.broadcast(Main.createPacket(9, Main.formatChatMessage("Their assets will now be liquidated")));
		for (Unit u : evt.player.getUnits()) {
			server.broadcast(Main.createPacket(11, u.getSpatialID()));
			server.unregister(u);
		}
		evt.player.username=null;
	}
	
	@SubscribeEvent(SYS)
	public void onWindowRequest(WindowRequestedEvent evt) {
		//TODO window system
	}
	
	
	@SubscribeEvent(SYS)
	public void onSlotSelected(WindowSlotSelectedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerMoveUnits(PlayerMoveUnitsEvent evt) {
		
	}
}
