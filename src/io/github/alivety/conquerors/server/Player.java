package io.github.alivety.conquerors.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.ppl.AbstractPacket;

public class Player extends Unit {
	private final SocketChannel ch;
	private final Server server;
	private final long created = new Date().getTime();
	protected String username = null;
	private int money;
	private final int mpm = 5;
	private AbstractPacket job;
	private final boolean works = true;
	private final List<String> unitsSpatialID = new ArrayList<String>();

	public Player(final SocketChannel ch, final Server server) {
		super(server);
		this.ch = ch;
		this.server = server;
	}

	private void write(final ByteBuffer buf) throws IOException {
		this.ch.write(buf);
	}

	public void write(final AbstractPacket p) {
		try {
			this.write(Main.encode(p));
		} catch (final IOException e) {
			Main.handleError(e);
		}
	}

	public void packet(final int pid, final Object... fields) {
		this.write(Main.createPacket(pid, fields));
	}

	public String username() {
		return this.username;
	}

	public AbstractPacket job() {
		return this.job;
	}

	@Override
	public String getUnitType() {
		return "player";
	}

	@Override
	public String getOwnerSpatialID() {
		return null;
	}

	@Override
	public String toString() {
		return this.username() + " - " + this.spatialID;
	}

	public String[] getUnitIDs() {
		return this.unitsSpatialID.toArray(new String[this.unitsSpatialID.size()]);
	}

	public Unit[] getUnits() {
		final String[] ids = this.getUnitIDs();
		final Unit[] units = new Unit[ids.length];
		for (int i = 0; i < units.length; i++)
			units[i] = this.server.unitBySpatialID(ids[i]);
		return units;
	}
}
