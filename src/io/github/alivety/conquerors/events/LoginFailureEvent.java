package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.PRET;

@Clientside
public class LoginFailureEvent extends PRET {
	public String reason;
	public LoginFailureEvent(String reason){
		this.reason=reason;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}

}
