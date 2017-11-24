package io.github.alivety.conquerors.common;

import com.jme3.math.Vector3f;

public abstract class UnitObject {
	protected final String spatialID;
	private Vector3f position;
	private int health = 20;
	
	protected UnitObject() {
		this.spatialID = Main.uuid(this.getUnitType());
	}
	
	protected UnitObject(final String spatialID) {
		this.spatialID = spatialID;
	}
	
	public abstract String getOwnerSpatialID();
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public final String getSpatialID() {
		return this.spatialID;
	}
	
	public abstract String getUnitType();
	
	public Vector3f move(final Vector3f hm) {
		this.position = this.position.add(hm);
		return this.position;
	}
	
	public Vector3f teleport(final Vector3f loc) {
		this.position = loc;
		return this.position;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void attackHealth(final int value) {
		this.health -= value;
	}
}
