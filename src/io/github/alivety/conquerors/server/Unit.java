package io.github.alivety.conquerors.server;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.Vector;

public abstract class Unit {
	protected final String spatialID;
	private Vector position;

	protected Unit(final Server server) {
		this.spatialID = Main.uuid(this.getUnitType());
		server.registerUnit(this);
	}

	public abstract String getUnitType();

	public abstract String getOwnerSpatialID();

	public final String getSpatialID() {
		return this.spatialID;
	}

	public Vector move(final Vector hm) {
		this.position = this.position.add(hm);
		return this.position;
	}

	public Vector teleport(final Vector loc) {
		this.position = loc;
		return this.position;
	}

	public Vector getPosition() {
		return this.position;
	}
}
