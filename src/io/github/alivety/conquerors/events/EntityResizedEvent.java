package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;

@Clientside
public class EntityResizedEvent extends PRET implements Cancelable {
	public String spatialID;
	public float x,y,z;
	public EntityResizedEvent(String spatialID,float x,float y,float z) {
		this.spatialID=spatialID;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}
}
