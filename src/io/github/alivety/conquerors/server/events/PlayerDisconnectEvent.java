package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Serverside;

@Serverside
public class PlayerDisconnectEvent extends PRET {
	public PlayerObject player;

	public PlayerDisconnectEvent(final PlayerObject player) {
		this.player = player;
	}

	@Override
	public void post() {

	}

}
