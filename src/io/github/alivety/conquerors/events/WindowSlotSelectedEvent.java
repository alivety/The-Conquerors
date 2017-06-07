package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class WindowSlotSelectedEvent extends PRET implements Cancelable {
	public Player player;
	public String spatialID;
	public int slot;
	public WindowSlotSelectedEvent(Player player,String spatialID,int slot) {
		this.player=player;
		this.spatialID=spatialID;
		this.slot=slot;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}

}
