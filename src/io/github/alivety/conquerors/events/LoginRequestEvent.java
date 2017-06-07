package io.github.alivety.conquerors.events;

import io.github.alivety.conquerors.Main;
import io.github.alivety.conquerors.event.Cancelable;
import io.github.alivety.conquerors.event.PRET;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class LoginRequestEvent extends PRET implements Cancelable {
	public Player client;
	public String username;
	public int protocolVersion;
	
	public LoginRequestEvent(Player client,String username,int protocolVersion) {
		this.client=client;
		this.username=username;
		this.protocolVersion=protocolVersion;
	}
	
	@Override
	public void post() {
		client.write(Main.createPacket(1, client.getSpatialID()));
		//TODO more stuff
	}

}