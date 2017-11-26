package io.github.alivety.conquerors.server;

import static io.github.alivety.conquerors.common.event.EventPriority.SYS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import io.github.alivety.conquerors.client.packets.PacketCreateModel;
import io.github.alivety.conquerors.client.packets.PacketTranslateEntity;
import io.github.alivety.conquerors.client.packets.PacketUpdateEntityOwnership;
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
		
		int teamColor=(int) (Math.random()*server.teams.length);
		while (server.usedTeams.contains(teamColor)) {
			teamColor=(int) (Math.random()*server.teams.length);
		}
		server.usedTeams.add(teamColor);
		
		evt.client.username(evt.username);
		evt.client.color=server.teams[teamColor];
		evt.client.packet(1, evt.client.getSpatialID(), server.teams[teamColor]);
		this.server.broadcast(Main.createPacket(12, new Object[] { this.server.playerList() }));
		this.server.broadcast(Main.createPacket(9, Main.formatChatMessage(evt.username + " has joined the game")));
		evt.client.packet(9, Main.formatChatMessage(evt.username + ".spatialID=" + evt.client.getSpatialID()));
		
		if (server.getOnlinePlayers().length<3) {
			server.broadcast(Main.createPacket(9, Main.formatChatMessage("There must be "+(3-server.getOnlinePlayers().length)+" more players to start the game")));
			server.begin=false;
		}
		
		evt.client.money=300;
		evt.client.mpm=0;
		
		//send currents
		System.out.println(server.units);
		for (Unit u:server.units.values().toArray(new Unit[]{})) {
			if (u==null) continue;
			PacketCreateModel pcm=new PacketCreateModel();
			pcm.spatialID=u.getSpatialID();
			pcm.position=new float[]{u.getPosition().x,u.getPosition().y,u.getPosition().z};
			pcm.form=u.getForm();
			PacketUpdateEntityOwnership pueo=new PacketUpdateEntityOwnership();
			pueo.spatialID=u.getSpatialID();
			pueo.ownerSpatialID=u.getOwnerSpatialID();
			evt.client.write(pcm);
			evt.client.write(pueo);
		}
		
		// TODO spawn starter items
		List<Unit> units=new ArrayList<>();
		units.add(new Unit(server){
			@Override
			public float[][] getForm() {
				return FormMaster.getCommandCenter(evt.client.color);
			}

			@Override
			public String getOwnerSpatialID() {
				return evt.client.getSpatialID();
			}

			@Override
			public String getUnitType() {
				return "CoCe";
			}});
		Vector3f position;
		if (evt.client.color.equals(ColorRGBA.White)) {
			position=new Vector3f(41,-268,315);
		} else if (evt.client.color.equals(ColorRGBA.Black)) {
			position=new Vector3f(190,-253,294);
		} else if (evt.client.color.equals(ColorRGBA.Blue)) {
			position=new Vector3f(-329,-204,253);
		} else if (evt.client.color.equals(ColorRGBA.Cyan)) {
			position=new Vector3f(474,-228,365);
		} else if (evt.client.color.equals(ColorRGBA.Gray)) {
			position=new Vector3f(-379,-249,-376);
		} else if (evt.client.color.equals(ColorRGBA.Green)) {
			position=new Vector3f(38,-219,-386);
		} else if (evt.client.color.equals(ColorRGBA.Magenta)) {
			position=new Vector3f(191,-205,-272);
		} else if (evt.client.color.equals(ColorRGBA.Pink)) {
			position=new Vector3f(287,-202,147);
		} else if (evt.client.color.equals(ColorRGBA.Red)) {
			position=new Vector3f(85,-235,475);
		} else if (evt.client.color.equals(ColorRGBA.Yellow)) {
			position=new Vector3f(-164,-268,474);
		} else {
			position=new Vector3f(0,0,0);
		}
		for (Unit u:units.toArray(new Unit[] {})) {
			if (!"CoCe".equals(u.getUnitType())) {
				
			}
			u.teleport(position);
			PacketCreateModel pcm=new PacketCreateModel();
			pcm.spatialID=u.getSpatialID();
			pcm.position=new float[]{position.x,position.y,position.z};
			pcm.form=u.getForm();
			PacketUpdateEntityOwnership pueo=new PacketUpdateEntityOwnership();
			pueo.spatialID=u.getSpatialID();
			pueo.ownerSpatialID=u.getOwnerSpatialID();
			server.broadcast(pcm);
			server.broadcast(pueo);
			
		}
		PacketTranslateEntity pte=new PacketTranslateEntity();
		pte.spatialID=evt.client.getSpatialID();
		pte.x=position.x;
		pte.y=position.y+5;
		pte.z=position.z;
		evt.client.write(pte);
		evt.client.addUnits(units);
		evt.client.packet(19, evt.client.money, evt.client.mpm, evt.client.getUnitSpatialIDs(), evt.client.getAlliesSpatialID());
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
		for (Unit u:server.units.values().toArray(new Unit[] {})) {
			if (u.getOwnerSpatialID().equals(evt.player.getSpatialID())) {
				this.server.broadcast(Main.createPacket(11, u.getSpatialID()));
				server.units.containsKey(server.unregister(u));
			}
		}
		for (int i=0;i<server.teams.length;i++) {
			if (server.teams[i].equals(evt.player.color)) {
				server.usedTeams.remove((Integer)i);
			}
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
					evt.player.packet(9, Main.formatChatMessage("You cannot use units until the game has began"));
					return;
				}
				//TODO window by unit
		}
	}
}
