package io.github.alivety.conquerors.common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import io.github.alivety.ppl.Packet;

public class PlayerObject extends UnitObject {
	private final SocketChannel ch;
	protected String username;
	public int money, mpm = 5;
	// private final long created = new Date().getTime();
	public boolean isReady = true;
	private final List<UnitObject> units = new ArrayList<UnitObject>();
	// private final List<PlayerObject> alliance = new
	// ArrayList<PlayerObject>();

	public PlayerObject(final SocketChannel ch) {
		this.ch = ch;
	}

	@Override
	public String getOwnerSpatialID() {
		return null;
	}

	public UnitObject[] getUnits() {
		return this.units.toArray(new UnitObject[this.units.size()]);
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
		return this.username() + " - " + this.spatialID;
	}

	public String username() {
		return this.username;
	}

	public void username(final String username) {
		this.username = username;
	}

	private void write(final ByteBuffer buff) throws IOException {
		if (!this.isReady) return;
		this.ch.write(buff);
	}

	public void write(final Packet p) {
		try {
			this.write(Main.encode(p));
		} catch (final IOException e) {
			Main.handleError(e);
		}
	}
}
