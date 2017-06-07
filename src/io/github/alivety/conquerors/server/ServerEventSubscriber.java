package io.github.alivety.conquerors.server;

import static io.github.alivety.conquerors.event.EventPriority.SYS;

import io.github.alivety.conquerors.event.SubscribeEvent;
import io.github.alivety.conquerors.events.LoginRequestEvent;
import io.github.alivety.conquerors.events.PlayerChatEvent;
import io.github.alivety.conquerors.events.PlayerDisconnectEvent;
import io.github.alivety.conquerors.events.PlayerMoveUnitsEvent;
import io.github.alivety.conquerors.events.PlayerMovedEvent;
import io.github.alivety.conquerors.events.WindowRequestedEvent;
import io.github.alivety.conquerors.events.WindowSlotSelectedEvent;

public class ServerEventSubscriber {
	@SubscribeEvent(SYS)
	public void onLoginRequest(LoginRequestEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerChat(PlayerChatEvent evt) {
		if (evt.client==null) return;
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerMove(PlayerMovedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerDisconnect(PlayerDisconnectEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onWindowRequest(WindowRequestedEvent evt) {
		
	}
	
	
	@SubscribeEvent(SYS)
	public void onSlotSelected(WindowSlotSelectedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerMoveUnits(PlayerMoveUnitsEvent evt) {
		
	}
}
