package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.event.PRET;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class PlayerDisconnectEvent extends PRET {
	public Player player;
	public PlayerDisconnectEvent(Player player) {
		this.player=player;
	}
	
	@Override
	public void post() {
		
	}

}
