package io.github.alivety.conquerors.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.jme3.math.ColorRGBA;

import io.github.alivety.ppl.PPLAdapter;
import io.github.alivety.ppl.packet.Packet;

public class PlayerObject extends UnitObject {
	private final PPLAdapter adapter;
	protected String username;
	public int money, mpm = 5;
	// private final long created = new Date().getTime();
	public boolean isReady = true;
	private final List<UnitObject> units = new ArrayList<UnitObject>();
	private final List<PlayerObject> alliance=new ArrayList<PlayerObject>();
	// private final List<PlayerObject> alliance = new
	// ArrayList<PlayerObject>();
	public ColorRGBA color;
	
	public PlayerObject(PPLAdapter adapter) {
		this.adapter=adapter;
	}

	@Override
	public String getOwnerSpatialID() {
		return null;
	}
	
	public UnitObject[] getUnits() {
		return this.units.toArray(new UnitObject[this.units.size()]);
	}
	
	public String[] getUnitSpatialIDs() {
		final UnitObject[] units = this.getUnits();
		final String[] rsp = new String[units.length];
		for (int i = 0; i < rsp.length; i++)
			rsp[i] = units[i].spatialID;
		return rsp;
	}
	
	@Override
	public String getUnitType() {
		return "Player";
	}
	
	public void addUnit(UnitObject u) {
		this.units.add(u);
	}
	
	public void addUnits(UnitObject...u) {
		this.units.addAll(Arrays.asList(u));
	}
	
	public void addUnits(Collection<? extends UnitObject> u) {
		this.units.addAll(u);
	}
	
	public void redefineUnits(List<UnitObject> list) {
		this.units.clear();
		this.units.addAll(list);
	}
	
	public void packet(final int pid, final Object... fields) {
		this.write(Main.createPacket(pid, fields));
	}
	
	@Override
	public String toString() {
		return this.username() + "{" + this.spatialID + "}";
	}
	
	public String username() {
		return this.username;
	}
	
	public void username(final String username) {
		this.username = username;
	}
	
	public void write(final Packet p) {
		try {
			Main.out.debug(this + ".write(" + p + ")");
			adapter.writePacket(p);
		} catch (final IOException e) {
			Main.handleError(e);
		}
	}
	
	public boolean isAlly(PlayerObject o) {
		return alliance.contains(o);
	}
	
	public void ally(PlayerObject o) {
		alliance.add(o);
	}
	
	public int allyCount() {
		int count=0;
		Iterator<PlayerObject> iter=alliance.iterator();
		while (iter.hasNext()) {
			if (iter.next().username()!=null) {
				count++;
			}
		}
		return count;
	}
	
	public String[] getAlliesSpatialID() {
		ArrayList<String> a=new ArrayList<>(this.allyCount());
		Iterator<PlayerObject> iter=alliance.iterator();
		while (iter.hasNext()) {
			PlayerObject o=iter.next();
			if (o.username()!=null) {
				a.add(o.spatialID);
			}
		}
		return a.toArray(new String[] {});
	}
}
