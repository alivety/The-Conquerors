package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class PlayerDisconnectEvent extends PRET {
	public Player player;

	public PlayerDisconnectEvent(final Player player) {
		this.player = player;
	}

	@Override
	public void post() {

	}

}
