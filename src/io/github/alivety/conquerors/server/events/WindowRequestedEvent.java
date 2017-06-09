package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class WindowRequestedEvent extends PRET implements Cancelable {
	public String spatialID;
	public Player player;

	public WindowRequestedEvent(final Player player, final String spatialID) {
		this.spatialID = spatialID;
		this.player = player;
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}

}
