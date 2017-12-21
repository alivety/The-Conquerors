package io.github.alivety.conquerors.client.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;

public class EntityRotateEvent extends Event implements Cancelable {
	public String spatialID;
	public float x, y, z;
	public float ux, uy, uz;
	
	public EntityRotateEvent(String spatialID, float x, float y, float z, float ux, float uy, float uz) {
		this.spatialID=spatialID;
		this.x=x;
		this.y=y;
		this.z=z;
		this.ux=ux;
		this.uy=uy;
		this.uz=uz;
	}
}
