package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;

@Clientside
public class EntitySpawnEvent extends PRET implements Cancelable {
	public String model,material,spatialID;
	public EntitySpawnEvent(String model,String material,String spatialID) {
		this.model=model;
		this.material=material;
		this.spatialID=spatialID;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}
}
