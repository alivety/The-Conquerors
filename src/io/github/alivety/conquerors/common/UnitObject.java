package io.github.alivety.conquerors.common;

public abstract class UnitObject {
	protected final String spatialID;
	private Vector position;
	private int health = 20;
	
	protected UnitObject() {
		this.spatialID = Main.uuid(this.getUnitType());
	}
	
	protected UnitObject(final String spatialID) {
		this.spatialID = spatialID;
	}
	
	public abstract String getOwnerSpatialID();
	
	public Vector getPosition() {
		return this.position;
	}
	
	public final String getSpatialID() {
		return this.spatialID;
	}
	
	public abstract String getUnitType();
	
	public Vector move(final Vector hm) {
		this.position = this.position.add(hm);
		return this.position;
	}
	
	public Vector teleport(final Vector loc) {
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
