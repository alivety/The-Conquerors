package io.github.alivety.conquerors.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import io.github.alivety.ppl.PPLAdapter;
import io.github.alivety.ppl.packet.Packet;

public class PlayerObject extends UnitObject {
	private final PPLAdapter adapter;
	protected String username;
	public int money, mpm = 5;
	// private final long created = new Date().getTime();
	public boolean isReady = true;
	private final List<UnitObject> units = new ArrayList<UnitObject>();
	// private final List<PlayerObject> alliance = new
	// ArrayList<PlayerObject>();
	
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
}
