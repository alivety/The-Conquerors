package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;

public class EntityRotateEvent extends Event implements Cancelable {
	String spatialID;
	public float x, y, z;
	
	public EntityRotateEvent(String spatialID, float x, float y, float z) {
		this.spatialID=spatialID;
		this.x=x;
		this.y=y;
		this.z=z;
	}
}
