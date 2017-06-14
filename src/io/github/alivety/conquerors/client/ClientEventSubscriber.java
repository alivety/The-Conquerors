package io.github.alivety.conquerors.client;

import io.github.alivety.conquerors.common.event.SubscribeEvent;

import static io.github.alivety.conquerors.common.event.EventPriority.*;

import io.github.alivety.conquerors.client.events.EntityMovedEvent;
import io.github.alivety.conquerors.client.events.EntityOwnershipChangedEvent;
import io.github.alivety.conquerors.client.events.EntityRemovedEvent;
import io.github.alivety.conquerors.client.events.EntityResizedEvent;
import io.github.alivety.conquerors.client.events.EntitySpawnEvent;
import io.github.alivety.conquerors.client.events.LoginFailureEvent;
import io.github.alivety.conquerors.client.events.LoginSuccessEvent;
import io.github.alivety.conquerors.client.events.PlayerListUpdatedEvent;
import io.github.alivety.conquerors.client.events.UpdatedPlayerVariablesEvent;
import io.github.alivety.conquerors.client.events.WindowOpenedEvent;

public class ClientEventSubscriber {
	private Client client;
	public ClientEventSubscriber(Client client) {
		this.client=client;
	}
	
	@SubscribeEvent(SYS)
	public void onEntityMove(EntityMovedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onEntityOwnershipUpdate(EntityOwnershipChangedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onEntityDespawn(EntityRemovedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onEntityScale(EntityResizedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onEntitySpawn(EntitySpawnEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onLoginFailure(LoginFailureEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onLoginSuccess(LoginSuccessEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerListUpdate(PlayerListUpdatedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerVariablesUpdate(UpdatedPlayerVariablesEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onWindow(WindowOpenedEvent evt) {
		
	}
}
