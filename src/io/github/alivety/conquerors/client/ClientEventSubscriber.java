package io.github.alivety.conquerors.client;

import static io.github.alivety.conquerors.common.event.EventPriority.SYS;

import java.io.IOException;

import javax.swing.JOptionPane;

import io.github.alivety.conquerors.client.events.ConnectEvent;
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
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.event.SubscribeEvent;
import io.github.alivety.conquerors.common.events.PlayerChatEvent;

public class ClientEventSubscriber {
	private final Client client;
	
	public ClientEventSubscriber(final Client client) {
		this.client = client;
	}
	
	@SubscribeEvent(SYS)
	public void onEntityMove(final EntityMovedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onEntityOwnershipUpdate(final EntityOwnershipChangedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onEntityDespawn(final EntityRemovedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onEntityScale(final EntityResizedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onEntitySpawn(final EntitySpawnEvent evt) {
		this.client.getApp().scheduleAddEntity(evt.spatialID, evt.material, evt.model);
	}
	
	@SubscribeEvent(SYS)
	public void onLoginFailure(final LoginFailureEvent evt) {
		JOptionPane.showMessageDialog(null, "Your login was not accepted: " + evt.reason);
		System.exit(0);
	}
	
	@SubscribeEvent(SYS)
	public void onLoginSuccess(final LoginSuccessEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerListUpdate(final PlayerListUpdatedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerVariablesUpdate(final PlayerVariablesUpdateEvent evt) {
		this.client.money = evt.money;
		this.client.mpm = evt.mpm;
		// TODO units
	}
	
	@SubscribeEvent(SYS)
	public void onWindowOpen(final WindowOpenedEvent evt) {
		
	}
	
	@SubscribeEvent(SYS)
	public void onConnect(final ConnectEvent evt) throws IOException {
		this.client.server = evt.ch;
		this.client.server.write(Main.encode(Main.createPacket(0, Main.PREFS.getUsername(), Main.PRO_VER)));
		this.client.initApp();
		this.client.getApp().start();
	}
	
	@SubscribeEvent(SYS)
	public void onChat(final PlayerChatEvent evt) {
		Main.out.info(evt.message);
	}
}
