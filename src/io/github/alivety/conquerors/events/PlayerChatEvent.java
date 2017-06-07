package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.Main;
import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;
import io.github.alivety.conquerors.server.Player;

@Common
public class PlayerChatEvent extends PRET implements Cancelable {
	public Player client;
	public String message;
	public PlayerChatEvent(Player player,String message) {
		this.client=player;
		this.message=message;
	}
	
	@Override
	public void post() {
		if (Main.server==0) {
			
		} else {
			
		}
		
		//TODO server-side and client-side
	}
	
}
