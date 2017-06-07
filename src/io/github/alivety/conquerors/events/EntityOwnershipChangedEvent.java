package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;

@Clientside
public class EntityOwnershipChangedEvent extends PRET implements Cancelable {
	public String spatialID,ownerSpatialID;
	public EntityOwnershipChangedEvent(String spatialID,String ownerSpatialID) {
		this.spatialID=spatialID;
		this.ownerSpatialID=ownerSpatialID;
	}
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}
	
}
