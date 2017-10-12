package io.github.alivety.conquerors.client;

import com.jme3.scene.Spatial;

import io.github.alivety.conquerors.common.UnitObject;

public class Entity extends UnitObject {
	private final Spatial spat;
	
	public Entity(final Spatial spat) {
		super(spat.getName());
		this.spat = spat;
	}
	
	@Override
	public String getUnitType() {
		return "Entity";
	}
	
	@Override
	public String getOwnerSpatialID() {
		return this.spat.getUserData("owner");
	}
}
