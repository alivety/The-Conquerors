package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.PRET;

@Clientside
public class UpdatedPlayerVariablesEvent extends PRET {
	public int money,mpm;
	public String[] unitSpatialID;
	public UpdatedPlayerVariablesEvent(int money, int mpm,String[]unitSpatialID) {
		this.money=money;
		this.mpm=mpm;
		this.unitSpatialID=unitSpatialID;
	}
	
	@Override
	public void post() {
		
	}
}
