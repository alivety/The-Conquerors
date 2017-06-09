package io.github.alivety.conquerors.server.events;

import io.github.alivety.conquerors.common.event.Cancelable;
import io.github.alivety.conquerors.common.event.PRET;
import io.github.alivety.conquerors.common.events.Serverside;
import io.github.alivety.conquerors.server.Player;

@Serverside
public class WindowSlotSelectedEvent extends PRET implements Cancelable {
	public Player player;
	public String spatialID;
	public int slot;

	public WindowSlotSelectedEvent(final Player player, final String spatialID, final int slot) {
		this.player = player;
		this.spatialID = spatialID;
		this.slot = slot;
	}

	@Override
	public void post() {
		// TODO Auto-generated method stub

	}

}
