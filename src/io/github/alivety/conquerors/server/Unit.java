package io.github.alivety.conquerors.server;

import io.github.alivety.conquerors.Main;
import io.github.alivety.conquerors.Vector;

public abstract class Unit {
	protected final String spatialID;
	private Vector position;
	protected Unit(Server server) {
		this.spatialID=Main.uuid(this.getUnitType());
		server.registerUnit(this);
	}
	
	public abstract String getUnitType();
	public abstract String getOwnerSpatialID();
	
	public final String getSpatialID() {
		return this.spatialID;
	}
	
	public Vector move(Vector hm) {
		this.position=position.add(hm);
		return position;
	}
	
	public Vector teleport(Vector loc) {
		this.position=loc;
		return position;
	}
	
	public Vector getPosition() {
		return position;
	}
}
