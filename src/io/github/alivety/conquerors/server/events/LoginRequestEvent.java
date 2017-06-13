package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Serverside;

@Serverside
public class LoginRequestEvent extends Event implements Cancelable {
	public PlayerObject client;
	public String username;
	public int protocolVersion;
	
	public LoginRequestEvent(final PlayerObject client, final String username, final int protocolVersion) {
		this.client = client;
		this.username = username;
		this.protocolVersion = protocolVersion;
	}
}
