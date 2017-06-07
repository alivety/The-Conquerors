package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class PlayerMoveUnitsEvent extends PRET implements Cancelable {
	public Player player;
	public String[] spatialID;
	public float x,y,z;
	public PlayerMoveUnitsEvent(Player player,String[]spatialID,float x,float y,float z) {
		this.player=player;
		this.spatialID=spatialID;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}
}
