package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Serverside;

@Serverside
public class PlayerDisconnectEvent extends Event {
	public PlayerObject player;

	public PlayerDisconnectEvent(final PlayerObject player) {
		this.player = player;
	}
}
