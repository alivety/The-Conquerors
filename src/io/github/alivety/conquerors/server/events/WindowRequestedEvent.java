package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.PlayerObject;
import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.Event;
import io.github.alivety.conquerors.common.events.Serverside;

@Serverside
public class WindowRequestedEvent extends Event implements Cancelable {
	public String spatialID;
	public PlayerObject player;

	public WindowRequestedEvent(final PlayerObject player, final String spatialID) {
		this.spatialID = spatialID;
		this.player = player;
	}
}
