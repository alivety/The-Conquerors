package io.github.alivety.conquerors.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.alivety.ppl.AbstractPacket;

public class PlayerObject extends UnitObject {
	private final SocketChannel ch;
	protected String username;
	private int money,mpm=5;
	private final long created = new Date().getTime();
	private boolean isReady=false;
	private final List<UnitObject> units = new ArrayList<UnitObject>();
	
	public PlayerObject(SocketChannel ch) {
		this.ch=ch;
	}
	
	@Override
	public String getUnitType() {
		return "Player";
	}
	
	@Override
	public String getOwnerSpatialID() {
		return null;
	}
	
	private void write(ByteBuffer buff) throws IOException {
		ch.write(buff);
	}
	
	public void write(AbstractPacket p) {
		try {
			this.write(Main.encode(p));
		} catch (IOException e) {
			Main.handleError(e);
		}
	}
	
	public void packet(final int pid, final Object... fields) {
		this.write(Main.createPacket(pid, fields));
	}
	
	public String username() {
		return this.username;
	}
	
	public void username(String username) {
		this.username=username;
	}
	
	public UnitObject[] getUnits() {
		return units.toArray(new UnitObject[units.size()]);
	}
	
	@Override
	public String toString() {
		return this.username() + " - " + this.spatialID;
	}
}
