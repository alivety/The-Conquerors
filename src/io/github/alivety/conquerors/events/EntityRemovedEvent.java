package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.PRET;

@Clientside
public class EntityRemovedEvent extends PRET {
	public String spatialID;
	public EntityRemovedEvent(String spatialID) {
		this.spatialID=spatialID;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}

}
