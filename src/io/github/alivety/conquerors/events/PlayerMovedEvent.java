package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class PlayerMovedEvent extends PRET implements Cancelable {
	public Player client;
	public float x,y,z;
	public PlayerMovedEvent(Player client,float x,float y,float z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	@Override
	public void post() {
		// TODO Auto-generated method stub
		
	}

}
