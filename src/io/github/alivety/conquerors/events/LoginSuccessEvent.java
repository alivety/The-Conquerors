package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.PRET;

@Clientside
public class LoginSuccessEvent extends PRET {
	public String spatialID;
	public LoginSuccessEvent(String spatialID) {
		this.spatialID=spatialID;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}
}
