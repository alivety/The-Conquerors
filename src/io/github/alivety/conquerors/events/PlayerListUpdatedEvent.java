package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.PRET;

@Clientside
public class PlayerListUpdatedEvent extends PRET {
	public String[] list;
	public PlayerListUpdatedEvent(String[]list) {
		this.list=list;
	}
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}
	
}
