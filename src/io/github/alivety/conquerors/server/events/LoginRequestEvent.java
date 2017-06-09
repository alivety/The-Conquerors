package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.Main;
import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class LoginRequestEvent extends PRET implements Cancelable {
	public Player client;
	public String username;
	public int protocolVersion;

	public LoginRequestEvent(final Player client, final String username, final int protocolVersion) {
		this.client = client;
		this.username = username;
		this.protocolVersion = protocolVersion;
	}

	@Override
	public void post() {
		this.client.write(Main.createPacket(1, this.client.getSpatialID()));
		// TODO more stuff
	}

}
