package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class WindowRequestedEvent extends PRET implements Cancelable {
	public String spatialID;
	public Player player;
	public WindowRequestedEvent(Player player,String spatialID) {
		this.spatialID=spatialID;
		this.player=player;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}

}
