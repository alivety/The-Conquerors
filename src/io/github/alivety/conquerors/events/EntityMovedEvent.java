package io.github.alivety.conquerors.events;

import javax.annotation.Nonnull;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;

@Clientside
public class EntityMovedEvent extends PRET implements Cancelable {
	public @Nonnull String spatialID;
	public @Nonnull float x,y,z;
	
	public EntityMovedEvent(String spatialID,float x,float y,float z) {
		this.spatialID=spatialID;
		this.x=x;this.y=y;this.z=z;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}

}
