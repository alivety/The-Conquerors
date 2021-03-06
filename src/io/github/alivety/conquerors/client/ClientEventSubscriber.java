package io.github.alivety.conquerors.client;

import static io.github.alivety.conquerors.common.event.EventPriority.SYS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.jme3.math.Vector3f;

import io.github.alivety.conquerors.client.events.ConnectEvent;
import io.github.alivety.conquerors.client.events.CreateModelEvent;
import io.github.alivety.conquerors.client.events.CreateModelSpatialEvent;
import io.github.alivety.conquerors.client.events.EntityMovedEvent;
import io.github.alivety.conquerors.client.events.EntityOwnershipChangedEvent;
import io.github.alivety.conquerors.client.events.EntityRemovedEvent;
import io.github.alivety.conquerors.client.events.EntityResizedEvent;
import io.github.alivety.conquerors.client.events.EntityRotateEvent;
import io.github.alivety.conquerors.client.events.EntitySpawnEvent;
import io.github.alivety.conquerors.client.events.LoginFailureEvent;
import io.github.alivety.conquerors.client.events.LoginSuccessEvent;
import io.github.alivety.conquerors.client.events.PlayerListUpdatedEvent;
import io.github.alivety.conquerors.client.events.PlayerVariablesUpdateEvent;
import io.github.alivety.conquerors.client.events.WindowOpenedEvent;
import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.UnitObject;
import io.github.alivety.conquerors.common.event.SubscribeEvent;
import io.github.alivety.conquerors.common.events.PlayerChatEvent;

public class ClientEventSubscriber {
	private final Client client;
	
	public ClientEventSubscriber(final Client client) {
		this.client = client;
	}
	
	@SubscribeEvent(SYS)
	public void onEntityRotate(EntityRotateEvent evt) {
		this.client.getApp().getSpatial(evt.spatialID).lookAt(new Vector3f(evt.x,evt.y,evt.z), new Vector3f(evt.ux,evt.uy,evt.uz));
	}
	
	@SubscribeEvent(SYS)
	public void onEntityMove(final EntityMovedEvent evt) {
		System.out.println(this.client.username());
		System.out.println(evt.spatialID);
		if (evt.spatialID.startsWith("Player")) {
			this.client.getApp().player.setPhysicsLocation(new Vector3f(evt.x, evt.y, evt.z));
		} else {
			this.client.getApp().getSpatial(evt.spatialID).setLocalTranslation(evt.x, evt.y, evt.z);
			this.client.getApp().getSpatialControl(evt.spatialID).setPhysicsLocation(new Vector3f(evt.x,evt.y,evt.z));
		}
	}
	
	@SubscribeEvent(SYS)
	public void onEntityOwnershipUpdate(final EntityOwnershipChangedEvent evt) {
		this.client.getApp().scheduleChangeEntityOwnership(evt.spatialID, evt.ownerSpatialID);
	}
	
	@SubscribeEvent(SYS)
	public void onEntityDespawn(final EntityRemovedEvent evt) {
		this.client.getApp().removeSpatial(evt.spatialID);
	}
	
	@SubscribeEvent(SYS)
	public void onEntityScale(final EntityResizedEvent evt) {
		this.client.getApp().getSpatial(evt.spatialID).scale(evt.x, evt.y, evt.z);
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
		this.client.username(evt.spatialID);
		Main.out.debug("SET CLIENT TO " + this.client.username());
		this.client.color = evt.team;
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerListUpdate(final PlayerListUpdatedEvent evt) {
		// TODO player list
	}
	
	@SubscribeEvent(SYS)
	public void onPlayerVariablesUpdate(final PlayerVariablesUpdateEvent evt) {
		this.client.money = evt.money;
		this.client.mpm = evt.mpm;
		final List<UnitObject> units = new ArrayList<>();
		for (final String id : evt.unitSpatialID)
			units.add(new UnitObject(id) {
				@Override
				public String getOwnerSpatialID() {
					return ClientEventSubscriber.this.client.getSpatialID();
				}
				
				@Override
				public String getUnitType() {
					return "unknown";
				}
			});
		for (final String id : evt.alliance)
			this.client.ally(id);
	}
	
	@SubscribeEvent(SYS)
	public void onWindowOpen(final WindowOpenedEvent evt) {
		// TODO window open
	}
	
	@SubscribeEvent(SYS)
	public void onConnect(final ConnectEvent evt) throws IOException {
		this.client.server = evt.adapter;
		this.client.server.writePacket(Main.createPacket(0, Main.PREFS.getUsername(), Main.PRO_VER));
		new Thread("game") {
			@Override
			public void run() {
				ClientEventSubscriber.this.client.initApp();
				ClientEventSubscriber.this.client.getApp().start();
			}
		}.start();
	}
	
	@SubscribeEvent(SYS)
	public void onChat(final PlayerChatEvent evt) {
		// TODO chat
	}
	
	@Deprecated
	@SubscribeEvent(SYS)
	public void onCreateModelSpatial(final CreateModelSpatialEvent evt) {}
	
	@SubscribeEvent(SYS)
	public void onCreateModel(final CreateModelEvent evt) {
		this.client.getApp().addModel(new Model.NetworkModel(evt));
	}
}
